package com.project.backend.services.impl.petition;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
import com.project.backend.models.petition.Petition;
import com.project.backend.repositories.repos.petition.PetitionRepository;
import com.project.backend.repositories.specification.PetitionSpecification;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.petition.CommentService;
import com.project.backend.services.inter.petition.PetitionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.backend.utils.SpecificationUtil.addSpecification;
import static com.project.backend.utils.SpecificationUtil.isValid;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {
    private final PetitionRepository petitionRepository;
    private final UserService userService;
    private final CommentService commentService;

    @Override
    public Petition create(Petition petition, long levelId, User creator, String targetName) {
        log.info("Service: Creating a new petition {}", petition);
        if (petition.getLevelType().equals(LevelType.GROUP_OF_PARENTS_AND_STUDENTS)
                || petition.getLevelType().equals(LevelType.GROUP_OF_TEACHERS)) {
            throw new IllegalArgumentException("Cannot create a petition with such level type.");
        }
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        petition.setCreationTime(now);
        petition.setEndTime(now.plusDays(45));
        petition.setCreator(creator);
        petition.setStatus(Status.ACTIVE);
        petition.setTargetId(levelId);
        petition.setTargetName(targetName);
        return petitionRepository.save(petition);
    }

    @Transactional
    @Override
    public void delete(long id) {
        log.info("Service: Deleting a petition {}", id);
        findById(id);
        commentService.deleteWithPetition(id);
        petitionRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteBy(LevelType levelType, long targetId) {
        log.info("Service: Deleting a petitions");
        List<Long> petitionIds = petitionRepository.findAll(
                        levelType.equals(LevelType.SCHOOL) ?
                                PetitionSpecification.bySchool(targetId) : PetitionSpecification.byClass(targetId))
                .stream().map(Petition::getId).toList();
        for (Long id : petitionIds) {
            commentService.deleteWithPetition(id);
        }
        petitionRepository.deleteAllByLevelTypeAndTargetId(levelType, targetId);
    }

    @Override
    public long support(long petitionId, User user) {
        log.info("Service: Support for petition {} by user {}", petitionId, user.getId());
        Petition petition = findById(petitionId);
        if (!(petition.getStatus().equals(Status.ACTIVE))) {
            throw new IllegalStateException("Petition is not active.");
        }
        if(!(petition.now())){
            petition.setStatus(Status.UNSUCCESSFUL);
            petitionRepository.save(petition);
            throw new IllegalStateException("Petition is ended.");
        }
        boolean ifCanSupport = petition.getUsers().add(user);
        if (!ifCanSupport) {
            throw new IllegalArgumentException("Cannot support petition because user is already petition");
        }
        petition.incrementCount();
        checkingStatus(petition);
        return petitionRepository.save(petition).getCount();
    }

    @Override
    public void checkingStatus(Petition petition) {
        log.info("Service: Checking status for petition {}", petition.getId());
        long needed = countAll(petition);
        if (petition.getCount() >= needed) {
            petition.setCountNeeded(needed);
            petition.setStatus(Status.WAITING_FOR_CONSIDERATION);
        } else {
            if (!petition.now()){
                petition.setStatus(Status.UNSUCCESSFUL);
            }
        }
    }

    @Override
    public long countAll(Petition petition) {
        log.info("Service: Count all petition {}", petition.getId());
        long countAll = petition.getLevelType().equals(LevelType.SCHOOL) ?
                userService.countAllBySchool(petition.getTargetId()) : userService.countAllByClass(petition.getTargetId());
        return (long) (Math.floor(countAll / 2.0) + 1);
    }

    @Override
    public void approve(long petitionId) {
        log.info("Service: Approving a petition {}", petitionId);
        Petition petition = findById(petitionId);
        if (!(petition.getStatus().equals(Status.WAITING_FOR_CONSIDERATION))) {
            throw new IllegalStateException("Petition is not waiting.");
        }
        petition.setStatus(Status.APPROVED);
        petitionRepository.save(petition);
    }

    @Override
    public void reject(long petitionId) {
        log.info("Service: Rejecting a petition {}", petitionId);
        Petition petition = findById(petitionId);
        if (!(petition.getStatus().equals(Status.WAITING_FOR_CONSIDERATION))) {
            throw new IllegalStateException("Petition is not waiting.");
        }
        petition.setStatus(Status.REJECTED);
        petitionRepository.save(petition);
    }

    @Override
    public Petition findById(long id) {
        log.info("Service: Finding a petition {}", id);
        return petitionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Petition with id " + id + " not found"));
    }

    @Override
    public List<Petition> findAllMy(long userId) {
        return petitionRepository.findAll(PetitionSpecification.byUser(userService.findById(userId)));
    }

    @Override
    public Page<Petition> findAllMy(String name, String status, int page, int size, long userId) {
        log.info("Service: Finding all my petitions with name {} and status {}", name, status);

        User user = userService.findById(userId);
        Specification<Petition> petitionSpecification = createSpecification(name, status);
        Specification<Petition> fullSpecification = petitionSpecification == null ?
                PetitionSpecification.byUserInClass(user).and(PetitionSpecification.byUserInSchool(user)) :
                petitionSpecification.and(PetitionSpecification.byUserInClass(user)).and(PetitionSpecification.byUserInSchool(user));

        return petitionRepository.findAll(
                fullSpecification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "endTime"))
        );
    }

    @Override
    public Page<Petition> findAllByCreator(String name, String status, int page, int size, long creatorId) {
        log.info("Service: Finding all petitions by creator {}, name {} and status {}", creatorId, name, status);

        Specification<Petition> petitionSpecification = createSpecification(name, status);
        Specification<Petition> fullSpecification = petitionSpecification == null ?
                PetitionSpecification.byCreator(creatorId) :
                petitionSpecification.and(PetitionSpecification.byCreator(creatorId));

        return petitionRepository.findAll(
                fullSpecification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "endTime")
                )
        );
    }

    @Override
    public Page<Petition> findAllForDirector(String name, String status, int page, int size) {
        log.info("Service: Finding all petitions for director, name {} and status {}", name, status);

        Specification<Petition> petitionSpecification = createSpecification(name, status);

        if (petitionSpecification == null) {
            return petitionRepository.findAll(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "endTime")));
        } else {
            return petitionRepository.findAll(
                    petitionSpecification, PageRequest.of(
                            page, size, Sort.by(
                                    Sort.Direction.ASC, "endTime")));
        }
    }

    @Override
    public void deletingUser(long userId) {
        log.info("Service: Deleting user {}", userId);
        petitionRepository.saveAll(petitionRepository.findAll(PetitionSpecification.byCreator(userId)).stream()
                .peek(petition -> petition.setCreator(userService.findUserByEmail("!deleted-user!@deleted.com"))).toList());
        deleteVoteByUser(userId);
    }

    @Override
    public void deleteVoteByUser(long userId) {
        petitionRepository.saveAll(
                petitionRepository.findAll(
                                PetitionSpecification.byUserWithVote(userService.findById(userId)))
                        .stream().peek(petition ->
                        {
                            if (petition.now()) {
                                petition.decrementCount();
                                checkingStatus(petition);
                            }
                        }).toList());
    }

    @Override
    public List<Petition> findAllByUserAndLevelClass(long userId){
        return petitionRepository.findAll(PetitionSpecification.byUserInClass(userService.findById(userId)));
    }

    @Override
    public List<Petition> findAllByClass(long classId){
        return petitionRepository.findAll(PetitionSpecification.byClass(classId));
    }

    private Specification<Petition> createSpecification(String name, String status) {
        log.info("Service: Creating a petition {}", name + " " + status);
        Specification<Petition> specification = null;

        if (isValid(name)) {
            specification = addSpecification(specification, PetitionSpecification::byName, name);
        }
        if (isValid(status)) {
            specification = addSpecification(specification, PetitionSpecification::byStatus, Status.valueOf(status));
        }

        return specification;
    }
}

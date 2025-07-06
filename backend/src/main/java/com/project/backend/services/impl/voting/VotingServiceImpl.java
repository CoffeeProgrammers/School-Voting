package com.project.backend.services.impl.voting;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.repos.voting.VotingRepository;
import com.project.backend.repositories.specification.VotingSpecification;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.voting.AnswerService;
import com.project.backend.services.inter.voting.VotingService;
import com.project.backend.services.inter.voting.VotingUserService;
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
import java.util.stream.Stream;

import static com.project.backend.utils.SpecificationUtil.addSpecification;
import static com.project.backend.utils.SpecificationUtil.isValid;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotingServiceImpl implements VotingService {

    private final VotingRepository votingRepository;
    private final AnswerService answerService;
    private final UserService userService;
    private final VotingUserService votingUserService;

    @Override
    public Voting create(Voting votingRequest, List<String> answer, List<Long> targetIds, long schoolId, long userId) {
        log.info("Service: Creating voting {}", votingRequest);
        if (targetIds == null || targetIds.isEmpty() ) {
            throw new IllegalArgumentException("targetIds is null or empty");
        }
        User creator = userService.findById(userId);
        votingRequest.setCreator(creator);
        votingRequest.setTargetId(
                votingRequest.getLevelType().equals(LevelType.SCHOOL) ? targetIds.get(0) :
                        votingRequest.getLevelType().equals(LevelType.CLASS) ? targetIds.get(0) : -1);
        Voting voting = votingRepository.save(votingRequest);
        answerService.create(answer, voting);
        Stream<User> users = null;
        switch (voting.getLevelType()) {
            case SCHOOL -> {
                log.info("Service: Adding users from school {}", schoolId);
                Long schoolIdFromRequest = targetIds.get(0);
                if(creator.getSchool().getId() != schoolId && schoolIdFromRequest != schoolId) {
                    throw new IllegalArgumentException("Can`t create voting and not be in that school");
                }
                users = userService.findAllBySchool(schoolId, userId).stream();
            }
            case CLASS -> {
                Long classId = targetIds.get(0);
                if (creator.getRole().equals("PARENT")){
                    throw new IllegalArgumentException("Can`t create voting and not be in that class or be not director or teacher");
                }
                if(creator.getRole().equals("STUDENT") && creator.getMyClass().getId() != classId) {
                    throw new IllegalArgumentException("Can`t create voting and not be in that class or be not director or teacher");
                }
                log.info("Service: Adding users from class {}", classId);
                users = userService.findAllByClass(classId, userId).stream();
            }
            case GROUP_OF_TEACHERS -> {
                log.info("Service: Adding users from group of teachers");
                users = targetIds.stream().map(userService::findById).filter(u -> u.getRole().equalsIgnoreCase("TEACHER"));
            }
            case GROUP_OF_PARENTS_AND_STUDENTS -> {
                log.info("Service: Adding users from group of parents and students");
                users = targetIds.stream().map(userService::findById).filter(u -> u.getRole().equalsIgnoreCase("PARENT") || u.getRole().equalsIgnoreCase("STUDENT"));
            }
        }
        List<User> usersToAdd = users.filter(u -> u.getSchool().getId() == schoolId).toList();
        log.info("Service: Users to add: {}, before filters: {}", usersToAdd.stream().map(User::getId).toList(), targetIds);
        votingUserService.create(voting, usersToAdd);
        voting.setCountAll(usersToAdd.size());
        return voting;
    }

    @Override
    public Voting update(Voting votingRequest, List<String> answer, long id) {
        log.info("Service: Updating voting with id {}", id);
        Voting oldVoting = findById(id);
        checkTimeForChanges(oldVoting);
        oldVoting.setName(votingRequest.getName());
        oldVoting.setDescription(votingRequest.getDescription());
        answerService.update(answer, oldVoting);
        return votingRepository.save(oldVoting);
    }

    @Override
    public void delete(long id) {
        log.info("Service: Deleting voting with id {}", id);
        checkTimeForChanges(findById(id));
        votingRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteBy(LevelType levelType, long targetId) {
        log.info("Service: Deleting votings with levelType {}", levelType);
        if (levelType.equals(LevelType.SCHOOL)) {
            votingRepository.deleteAllByCreator_School_Id(targetId);
        } else {
            votingRepository.deleteAllByCreator_MyClass_Id(targetId);
        }
    }

    @Override
    public Voting findById(long id) {
        log.info("Service: Finding voting with id {}", id);
        return votingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Voting with id: " + id + " not found"));
    }

    @Override
    public List<Voting> findAllByUser(long userId) {
        log.info("Service: get list of votings where user id = {}", userId);
        return votingRepository.findAll(VotingSpecification.byUser(userId));
    }

    @Override
    public List<Voting> findAllByUserAndLevelClass(long userId) {
        log.info("Service: get list of votings where user id = {} and class typy", userId);
        return votingRepository.findAll(VotingSpecification.byUserInClass(userId));
    }

    @Override
    public List<Voting> findAllByClass(long classId){
        log.info("Service: get list of votings where class id = {}", classId);
        return votingRepository.findAll(VotingSpecification.byClass(classId));
    }

    @Override
    public Page<Voting> findAllByUser(
            long userId, String name, Boolean now, Boolean isNotVoted, int page, int size) {
        log.info("Service: Finding all votings by user {}", userId);
        Specification<Voting> voitingSpecification = createSpecification(name, false, now, null, isNotVoted, userId);
        Specification<Voting> fullSpecification = voitingSpecification == null ? VotingSpecification.byUser(userId) : voitingSpecification.and(VotingSpecification.byUser(userId));

        return votingRepository.findAll(
                fullSpecification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime")));
    }

    @Override
    public Page<Voting> findAllByCreator(
            long userId, String name, Boolean now, Boolean notStarted, int page, int size) {
        log.info("Service: Finding all votings by creator {}", userId);

        Specification<Voting> voitingSpecification = createSpecification(name, true, now, notStarted, null, null);
        Specification<Voting> fullSpecification = voitingSpecification == null ? VotingSpecification.byCreator(userId) : voitingSpecification.and(VotingSpecification.byCreator(userId));

        return votingRepository.findAll(
                fullSpecification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime")));
    }

    @Override
    public Page<Voting> findAllForDirector(
            long userId, String name, int page, int size) {
        log.info("Service: Finding all votings for director {}", userId);

        Specification<Voting> voitingSpecification = createSpecification(name, false, null, null, null, null);
        Specification<Voting> fullSpecification = voitingSpecification == null ? VotingSpecification.byDirector(userId) : voitingSpecification.and(VotingSpecification.byDirector(userId));

        return votingRepository.findAll(
                fullSpecification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime")));
    }

    @Override
    public void vote(long votingId, long answerId, User user) {
        log.info("Service: Vote for answer with id {}", answerId);
        Voting voting = findById(votingId);
        Answer answer = answerService.findById(answerId);
        if(answer.getVoting().getId() != votingId){
            throw new EntityNotFoundException("Cant answer answer with id: " + answerId + " because it`s not in voting");
        }
        checkTimeForVote(voting);
        answerService.vote(answerId);
        votingUserService.update(voting, user, answer);
    }

    @Transactional
    @Override
    public void deletingUser(long userId){
        log.info("Service: Deleting user with id {}", userId);
        List<Voting> votings = votingRepository.findAll(VotingSpecification.byCreator(userId))
                .stream().peek(voting -> voting.setCreator(userService.findUserByEmail("!deleted-user!@deleted.com"))).toList();
        votingRepository.saveAll(votings);
    }

    private void checkTimeForChanges(Voting voting) {
        log.info("Service: Checking time for changes {}", voting.getId());
        if (LocalDateTime.now().isAfter(voting.getStartTime())) {
            throw new IllegalArgumentException("Voting start time cannot be less than now for delete or update");
        }
    }

    private void checkTimeForVote(Voting voting) {
        log.info("Service: Checking time for voting {}", voting.getId());
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(voting.getStartTime())) {
            throw new IllegalArgumentException("Voting start time cannot be more than now for vote");
        }
        if(now.isAfter(voting.getEndTime())) {
            throw new IllegalArgumentException("Voting end time cannot be less than now for vote");
        }
    }

    private Specification<Voting> createSpecification(String name, boolean my, Boolean now, Boolean notStarted, Boolean canVote, Long userId) {
        log.info("Service: Creating specification with name {}, now {}, can vote {} and user {}", name, now, canVote, userId);
        Specification<Voting> specification = null;

        if (isValid(name)) {
            specification = addSpecification(specification, VotingSpecification::byName, name);
        }

        if (isValid(now)) {
            specification = addSpecification(specification, now ? VotingSpecification::byStartTimeAndEndTime : VotingSpecification::ended);
        } else {
            if (!my) {
                specification = addSpecification(specification, VotingSpecification::byStartTime);
            }
            if (isValid(notStarted) && notStarted) {
                specification = addSpecification(specification, VotingSpecification::byStartTimeNot);
            }
        }

        if (isValid(canVote)) {
            specification = addSpecification(specification, canVote ? VotingSpecification::isNotVoted : VotingSpecification::isVoted, userId);
        }

        return specification;
    }
}

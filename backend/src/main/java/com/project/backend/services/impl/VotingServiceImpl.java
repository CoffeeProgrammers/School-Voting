package com.project.backend.services.impl;

import com.project.backend.models.User;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.specification.UserSpecification;
import com.project.backend.repositories.specification.VotingSpecification;
import com.project.backend.repositories.voting.VotingRepository;
import com.project.backend.services.inter.AnswerService;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.VotingService;
import com.project.backend.services.inter.VotingUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
        votingRequest.setCreator(userService.findById(userId));
        Voting voting = votingRepository.save(votingRequest);
        answerService.create(answer, voting);
        Stream<User> users = null;
        switch (voting.getLevelType()) {
            case SCHOOL -> {
                log.info("Service: Adding users from school {}", schoolId);
                users = userService.findAllBySchool(schoolId, userId).stream();
            }
            case CLASS -> {
                Long classId = targetIds.get(0);
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
        return voting;
    }

    @Override
    public Voting update(Voting votingRequest, List<String> answer, long id) {
        log.info("Service: Updating voting with id {}", id);
        Voting oldVoting = findById(votingRequest.getId());
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

    @Override
    public Voting findById(long id) {
        log.info("Service: Finding voting with id {}", id);
        return votingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Voting with id: " + id + " not found"));
    }

    @Override
    public Page<Voting> findAllByUser(
            long userId, String name, Boolean now, Boolean canVote, int page, int size) {
        log.info("Service: Finding all votings by user {}", userId);
        Specification<Voting> voitingSpecification = createSpecification(name, false, now, null,  canVote, userId);
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
        checkTimeForVote(voting);
        votingUserService.update(voting, user, answerService.findById(answerId));
        answerService.vote(answerId);
    }

    private void checkTimeForChanges(Voting voting) {
        log.info("Service: Checking time for changes {}", voting.getId());
        if (LocalDateTime.now().isAfter(voting.getStartTime())) {
            throw new IllegalArgumentException("Voting start time cannot be less than now for delete or update");
        }
    }

    private void checkTimeForVote(Voting voting) {
        log.info("Service: Checking time for voting {}", voting.getId());
        if (LocalDateTime.now().isBefore(voting.getStartTime())) {
            throw new IllegalArgumentException("Voting start time cannot be less than now for vote");
        }
    }

    private Specification<Voting> createSpecification(String name, boolean my, Boolean now, Boolean notStarted, Boolean canVote, Long userId) {
        log.info("Service: Creating specification with name {}, now {}, can vote {} and user {}", name, now, canVote, userId);
        Specification<Voting> specification = null;

        if (isValid(name)) {
            specification = addSpecification(specification, VotingSpecification::byName, name);
        }

        if (isValid(now)) {
            specification = addSpecification(specification, now ? VotingSpecification::byStartDateAndEndDate : VotingSpecification::ended);
        } else {
            if (!my) {
                specification = addSpecification(specification, VotingSpecification::byStartDate);
            }
            if(isValid(notStarted) && notStarted) {
                specification = addSpecification(specification, VotingSpecification::byStartDateNot);
            }
        }

        if (isValid(canVote)) {
            specification = addSpecification(specification, canVote ? VotingSpecification::canVote : VotingSpecification::cantVote, userId);
        }

        return specification;
    }
}

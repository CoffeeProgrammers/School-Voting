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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

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
    public Voting create(Voting votingRequest, List<String> answer) {
        log.info("Creating voting {}", votingRequest);
        Voting voting = votingRepository.save(votingRequest);
        answerService.create(answer, voting);
        return voting;
    }

    @Override
    public Voting update(Voting votingRequest, List<String> answer, long id) {
        log.info("Updating voting with id {}", id);
        Voting oldVoting = findById(votingRequest.getId());
        checkTimeForChanges(oldVoting);
        oldVoting.setName(votingRequest.getName());
        oldVoting.setDescription(votingRequest.getDescription());
        answerService.update(answer, oldVoting);
        return votingRepository.save(oldVoting);
    }

    @Override
    public void delete(long id) {
        log.info("Deleting voting with id {}", id);
        checkTimeForChanges(findById(id));
        votingRepository.deleteById(id);
    }

    @Override
    public Voting findById(long id) {
        log.info("Finding voting with id {}", id);
        return votingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Voting with id: " + id + " not found"));
    }

    @Override
    public Page<Voting> findAllByUser(
            Authentication auth, String name, Boolean now, Boolean canVote, int page, int size) {
        long userId = userService.findUserByAuth(auth).getId();
        log.info("Finding all votings by user {}", userId);
        return votingRepository.findAll(
                VotingSpecification.byUser(userId)
                        .and(createSpecification(name, now, canVote, userId)),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime")));
    }

    @Override
    public Page<Voting> findAllByCreator(
            Authentication auth, String name, Boolean now, int page, int size) {
        long userId = userService.findUserByAuth(auth).getId();
        log.info("Finding all votings by creator {}", userId);

        return votingRepository.findAll(
                VotingSpecification.byCreator(userId)
                        .and(createSpecification(name, now, null, null)),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime")));
    }

    @Override
    public Page<Voting> findAllForDirector(
            Authentication auth, String name, int page, int size) {
        long userId = userService.findUserByAuth(auth).getId();
        log.info("Finding all votings for director {}", userId);
        return votingRepository.findAll(
                VotingSpecification.byDirector(userId)
                        .and(createSpecification(name, null, null, null)),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime")));
    }

    @Override
    public void vote(long votingId, long answerId, Authentication auth) {
        log.info("Vote voting with id {}", votingId);
        Voting voting = findById(votingId);
        checkTimeForChanges(voting);
        votingUserService.create(voting, userService.findUserByAuth(auth), answerService.findById(answerId));
        answerService.vote(answerId);
    }

    private void checkTimeForChanges(Voting voting){
        log.info("Checking time for changes {}", voting.getId());
        if(LocalDateTime.now().isAfter(voting.getStartTime())){
            throw new IllegalArgumentException("Voting start time cannot be less than now for delete or update");
        }
    }

    private void checkTimeForVote(Voting voting){
        log.info("Checking time for voting {}", voting.getId());
        if(LocalDateTime.now().isBefore(voting.getStartTime())){
            throw new IllegalArgumentException("Voting start time cannot be less than now for vote");
        }
    }

    private Specification<Voting> createSpecification(String name, Boolean now, Boolean canVote, Long userId) {
        Specification<Voting> specification = null;

        if(isValid(name)){
            specification = addSpecification(specification, VotingSpecification::byName, name);
        }
        if(isValid(now)){
            specification =  addSpecification(specification, now ? VotingSpecification::byStartDateAndEndDate : VotingSpecification::ended);
        }
        if(isValid(canVote)){
            specification =  addSpecification(specification, canVote ? VotingSpecification::canVote : VotingSpecification::cantVote, userId);
        }

        return specification;
    }
}

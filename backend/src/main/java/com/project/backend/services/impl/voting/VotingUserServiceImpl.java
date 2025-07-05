package com.project.backend.services.impl.voting;

import com.project.backend.models.User;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.models.voting.VotingUser;
import com.project.backend.repositories.repos.votings.VotingUserRepository;
import com.project.backend.services.inter.voting.AnswerService;
import com.project.backend.services.inter.voting.VotingUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotingUserServiceImpl implements VotingUserService {
    private final VotingUserRepository votingUserRepository;
    private final AnswerService answerService;

    @Override
    public List<VotingUser> create(Voting voting, List<User> users) {
        log.info("Service: Creating voting {} users for users", voting.getId());
        List<VotingUser> result = new ArrayList<>();
        users.forEach(u -> {
            result.add(new VotingUser(voting, u));
        });
        return votingUserRepository.saveAll(result);
    }

    @Override
    public VotingUser update(Voting voting, User user, Answer answer) {
        log.info("Service: Updating voting {} users for user {} with answer {}", voting.getId(), user.getId(), answer.getId());
        VotingUser votingUser = findById(voting.getId(), user.getId());
        votingUser.setAnswer(answer);
        return votingUserRepository.save(votingUser);
    }

    @Override
    public VotingUser findById(long votingId, long userId) {
        log.info("Service: Finding voting user with voting {} and user {}", votingId, userId);
        VotingUser.VotingUserId votingUserId = new VotingUser.VotingUserId(userId, votingId);
        return votingUserRepository.findById(votingUserId).orElseThrow(
                () -> new EntityNotFoundException("VotingUser with votingId " + votingId + " userId " + userId + " not found"));
    }

    @Override
    public boolean existsById(long votingId, long userId) {
        return votingUserRepository.existsById(new VotingUser.VotingUserId(userId, votingId));
    }

    @Override
    public Long countAllByVoting(long votingId) {
        log.info("Service: Counting all votingUsers by votingId {}", votingId);
        return votingUserRepository.countAllByVoting_Id(votingId);
    }

    @Override
    public Long countAllByVotingAnswered(long votingId) {
        log.info("Service: Counting all votingUsers by votingId {}, answered", votingId);
        return votingUserRepository.countAllByVoting_IdAndAnswerNotNull(votingId);
    }

    @Transactional
    @Override
    public void deleteWithUser(long userId) {
        List<VotingUser> votingUsers = votingUserRepository.findAllByUser_Id(userId);
        if (votingUsers.isEmpty()) {
            return;
        }
        Answer answer;
        for (VotingUser votingUser : votingUsers) {
            if(votingUser.getVoting().now()) {
                answer = votingUser.getAnswer();
                if (answer != null) {
                    answerService.decrement(answer.getId());
                }
            }
        }
        votingUserRepository.deleteAll(votingUsers);
    }
}

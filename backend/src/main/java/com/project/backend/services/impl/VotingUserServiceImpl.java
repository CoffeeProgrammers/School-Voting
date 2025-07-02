package com.project.backend.services.impl;

import com.project.backend.models.User;
import com.project.backend.models.VotingUser;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.voting.VotingUserRepository;
import com.project.backend.services.inter.VotingUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VotingUserServiceImpl implements VotingUserService {
    private final VotingUserRepository votingUserRepository;

    @Override
    public List<VotingUser> create(Voting voting, List<User> user) {
        List<VotingUser> result = new ArrayList<>();
        user.forEach(u -> {
            result.add(new VotingUser(voting, u));
        });
        return votingUserRepository.saveAll(result);
    }

    @Override
    public VotingUser update(Voting voting, User user, Answer answer) {
        VotingUser votingUser = findById(voting.getId(), user.getId());
        votingUser.setAnswer(answer);
        return votingUserRepository.save(votingUser);
    }

    @Override
    public VotingUser findById(long votingId, long userId) {
        VotingUser.VotingUserId votingUserId = new VotingUser.VotingUserId(votingId, userId);
        return votingUserRepository.findById(votingUserId).orElseThrow(() -> new EntityNotFoundException("VotingUser with votingId " + votingId + " userId " + userId + " not found"));
    }
}

package com.project.backend.services.inter;

import com.project.backend.models.User;
import com.project.backend.models.VotingUser;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;

import java.util.List;

public interface VotingUserService {
    List<VotingUser> create(Voting voting, List<User> user);
    VotingUser update(Voting voting, User user, Answer answer);

    VotingUser findById(long votingId, long userId);
}

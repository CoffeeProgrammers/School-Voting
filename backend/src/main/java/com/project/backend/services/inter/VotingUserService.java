package com.project.backend.services.inter;

import com.project.backend.models.User;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.models.voting.VotingUser;

import java.util.List;

public interface VotingUserService {
    List<VotingUser> create(Voting voting, List<User> user);
    VotingUser update(Voting voting, User user, Answer answer);
    VotingUser findById(long votingId, long userId);
    boolean exitsById(long votingId, long userId);

    Long countAllByVoting(long votingId);
    Long countAllByVotingAnswered(long votingId);

    void deleteWithUser(long userId);
}

package com.project.backend.services.inter;

import com.project.backend.models.User;
import com.project.backend.models.VotingUser;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;

public interface VotingUserService {
    VotingUser create(Voting voting, User user);
    VotingUser create(Voting voting, User user, Answer answer);
}

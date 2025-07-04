package com.project.backend.services.inter;

import com.project.backend.models.User;
import com.project.backend.models.UserVotingEvent;
import com.project.backend.models.voting.Voting;

import java.util.List;

public interface UserVotingEventService {
    UserVotingEvent create(User user, Voting voting, String eventId);

    UserVotingEvent findByUserAndVoting(long userId, long votingId);

    void delete(long userId, long votingId);

    List<UserVotingEvent> findAllByUser(long userId);

    List<UserVotingEvent> findAllByVoting(long votingId);
}

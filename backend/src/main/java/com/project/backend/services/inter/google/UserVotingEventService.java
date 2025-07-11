package com.project.backend.services.inter.google;

import com.project.backend.models.User;
import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.voting.Voting;

import java.util.List;

public interface UserVotingEventService {
    UserVotingEvent create(User user, Voting voting, String eventId, String reminderEventId);

    UserVotingEvent findByUserAndVoting(long userId, long votingId);

    void delete(long userId, long votingId);

    List<UserVotingEvent> findAllByUser(long userId);

    List<UserVotingEvent> findAllByVoting(long votingId);

    void deleteByUser(long userId);
}

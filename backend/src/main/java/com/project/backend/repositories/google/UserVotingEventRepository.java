package com.project.backend.repositories.google;

import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.google.UserVotingEvent.UserVotingEventId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVotingEventRepository extends JpaRepository<UserVotingEvent, UserVotingEventId> {
    List<UserVotingEvent> findAllById_UserId(long userId);
    List<UserVotingEvent> findAllById_VotingId(long votingId);
    Optional<UserVotingEvent> findById_UserIdAndId_VotingId(long userId, long votingId);
}

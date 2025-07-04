package com.project.backend.services.impl;

import com.project.backend.models.User;
import com.project.backend.models.UserVotingEvent;
import com.project.backend.models.UserVotingEvent.UserVotingEventId;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.UserVotingEventRepository;
import com.project.backend.services.inter.UserVotingEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserVotingEventServiceImpl implements UserVotingEventService {

    private final UserVotingEventRepository userVotingEventRepository;

    @Override
    public UserVotingEvent create(User user, Voting voting, String eventId) {
        UserVotingEventId id = new UserVotingEventId(user.getId(), voting.getId());
        return userVotingEventRepository.findById(id).orElseGet(() -> {
            UserVotingEvent event = new UserVotingEvent();
            event.setId(id);
            event.setUser(user);
            event.setVoting(voting);
            event.setEventId(eventId);
            return userVotingEventRepository.save(event);
        });
    }

    @Override
    public UserVotingEvent findByUserAndVoting(long userId, long votingId) {
        return userVotingEventRepository.findById_UserIdAndId_VotingId(userId, votingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "UserVotingEvent with id " + userId + " : " + votingId + " not found"));
    }

    @Override
    public void delete(long userId, long votingId) {
        userVotingEventRepository.deleteById(new UserVotingEventId(userId, votingId));
    }

    @Override
    public List<UserVotingEvent> findAllByUser(long userId) {
        return userVotingEventRepository.findAllById_UserId(userId);
    }

    @Override
    public List<UserVotingEvent> findAllByVoting(long votingId) {
        return userVotingEventRepository.findAllById_VotingId(votingId);
    }
}

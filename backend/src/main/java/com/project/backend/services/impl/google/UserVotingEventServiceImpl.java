package com.project.backend.services.impl.google;

import com.project.backend.models.User;
import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.google.UserVotingEvent.UserVotingEventId;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.repos.google.UserVotingEventRepository;
import com.project.backend.services.inter.google.UserVotingEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserVotingEventServiceImpl implements UserVotingEventService {

    private final UserVotingEventRepository userVotingEventRepository;

    @Override
    public UserVotingEvent create(User user, Voting voting, String eventId, String reminderEventId) {
        log.info("Service: Create user voting event");
        UserVotingEventId id = new UserVotingEventId(user.getId(), voting.getId());
        return userVotingEventRepository.findById(id).orElseGet(() -> {
            UserVotingEvent event = new UserVotingEvent();
            event.setId(id);
            event.setUser(user);
            event.setVoting(voting);
            event.setEventId(eventId);
            event.setReminderEventId(reminderEventId);
            return userVotingEventRepository.save(event);
        });
    }

    @Override
    public UserVotingEvent findByUserAndVoting(long userId, long votingId) {
        log.info("Service: Find user voting event with user {} and vouting {}",  userId, votingId);
        return userVotingEventRepository.findById_UserIdAndId_VotingId(userId, votingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "UserVotingEvent with id " + userId + " : " + votingId + " not found"));
    }

    @Override
    public void delete(long userId, long votingId) {
        log.info("Service: Delete user voting event");
        userVotingEventRepository.deleteById(new UserVotingEventId(userId, votingId));
    }

    @Override
    public List<UserVotingEvent> findAllByUser(long userId) {
        log.info("Service: Find all user`s voting events with user id {}", userId);
        return userVotingEventRepository.findAllById_UserId(userId);
    }

    @Override
    public List<UserVotingEvent> findAllByVoting(long votingId) {
        log.info("Service: Find all user`s voting events with voting id {}", votingId);
        return userVotingEventRepository.findAllById_VotingId(votingId);
    }

    @Transactional
    @Override
    public void deleteByUser(long userId){
        log.info("Service: Delete user voting events with user id {}", userId);
        userVotingEventRepository.deleteAllByUser_Id(userId);
    }
}

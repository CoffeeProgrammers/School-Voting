package com.project.backend.services.impl.google;

import com.project.backend.models.User;
import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.google.UserVotingEvent.UserVotingEventId;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.repos.google.UserVotingEventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserVotingEventServiceImplTest {

    @Mock
    private UserVotingEventRepository userVotingEventRepository;

    @InjectMocks
    private UserVotingEventServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldReturnExistingEventIfPresent() {
        User user = new User();
        user.setId(1L);
        Voting voting = new Voting();
        voting.setId(2L);
        String eventId = "e1";
        String reminderId = "r1";

        UserVotingEventId id = new UserVotingEventId(user.getId(), voting.getId());
        UserVotingEvent existingEvent = new UserVotingEvent();
        existingEvent.setId(id);
        existingEvent.setUser(user);
        existingEvent.setVoting(voting);
        existingEvent.setEventId(eventId);
        existingEvent.setReminderEventId(reminderId);

        when(userVotingEventRepository.findById(id)).thenReturn(Optional.of(existingEvent));

        UserVotingEvent result = service.create(user, voting, eventId, reminderId);

        assertEquals(existingEvent, result);
        verify(userVotingEventRepository).findById(id);
        verify(userVotingEventRepository, never()).save(any());
    }

    @Test
    void create_shouldSaveAndReturnNewEventIfNotExists() {
        User user = new User();
        user.setId(1L);
        Voting voting = new Voting();
        voting.setId(2L);
        String eventId = "e1";
        String reminderId = "r1";

        UserVotingEventId id = new UserVotingEventId(user.getId(), voting.getId());

        when(userVotingEventRepository.findById(id)).thenReturn(Optional.empty());

        UserVotingEvent savedEvent = new UserVotingEvent();
        savedEvent.setId(id);
        savedEvent.setUser(user);
        savedEvent.setVoting(voting);
        savedEvent.setEventId(eventId);
        savedEvent.setReminderEventId(reminderId);

        when(userVotingEventRepository.save(any(UserVotingEvent.class))).thenReturn(savedEvent);

        UserVotingEvent result = service.create(user, voting, eventId, reminderId);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(voting, result.getVoting());
        assertEquals(eventId, result.getEventId());
        assertEquals(reminderId, result.getReminderEventId());

        verify(userVotingEventRepository).findById(id);
        verify(userVotingEventRepository).save(any(UserVotingEvent.class));
    }

    @Test
    void findByUserAndVoting_shouldReturnEventIfFound() {
        long userId = 1L;
        long votingId = 2L;

        UserVotingEventId id = new UserVotingEventId(userId, votingId);
        UserVotingEvent event = new UserVotingEvent();
        event.setId(id);

        when(userVotingEventRepository.findById_UserIdAndId_VotingId(userId, votingId)).thenReturn(Optional.of(event));

        UserVotingEvent result = service.findByUserAndVoting(userId, votingId);

        assertEquals(event, result);
        verify(userVotingEventRepository).findById_UserIdAndId_VotingId(userId, votingId);
    }

    @Test
    void findByUserAndVoting_shouldThrowIfNotFound() {
        long userId = 1L;
        long votingId = 2L;

        when(userVotingEventRepository.findById_UserIdAndId_VotingId(userId, votingId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByUserAndVoting(userId, votingId));

        assertTrue(ex.getMessage().contains("UserVotingEvent with id"));
        verify(userVotingEventRepository).findById_UserIdAndId_VotingId(userId, votingId);
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        long userId = 1L;
        long votingId = 2L;

        UserVotingEventId id = new UserVotingEventId(userId, votingId);

        doNothing().when(userVotingEventRepository).deleteById(id);

        service.delete(userId, votingId);

        verify(userVotingEventRepository).deleteById(id);
    }

    @Test
    void findAllByUser_shouldReturnList() {
        long userId = 1L;
        List<UserVotingEvent> list = List.of(new UserVotingEvent());

        when(userVotingEventRepository.findAllById_UserId(userId)).thenReturn(list);

        List<UserVotingEvent> result = service.findAllByUser(userId);

        assertEquals(list, result);
        verify(userVotingEventRepository).findAllById_UserId(userId);
    }

    @Test
    void findAllByVoting_shouldReturnList() {
        long votingId = 2L;
        List<UserVotingEvent> list = List.of(new UserVotingEvent());

        when(userVotingEventRepository.findAllById_VotingId(votingId)).thenReturn(list);

        List<UserVotingEvent> result = service.findAllByVoting(votingId);

        assertEquals(list, result);
        verify(userVotingEventRepository).findAllById_VotingId(votingId);
    }

    @Test
    void deleteByUser_shouldCallDeleteAllByUserId() {
        long userId = 1L;

        doNothing().when(userVotingEventRepository).deleteAllByUser_Id(userId);

        service.deleteByUser(userId);

        verify(userVotingEventRepository).deleteAllByUser_Id(userId);
    }
}

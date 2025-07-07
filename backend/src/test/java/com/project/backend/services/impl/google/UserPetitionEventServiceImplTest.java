package com.project.backend.services.impl.google;

import com.project.backend.models.User;
import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.google.UserPetitionEvent.UserPetitionEventId;
import com.project.backend.models.petition.Petition;
import com.project.backend.repositories.repos.google.UserPetitionEventRepository;
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

class UserPetitionEventServiceImplTest {

    @Mock
    private UserPetitionEventRepository userPetitionEventRepository;

    @InjectMocks
    private UserPetitionEventServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldReturnExistingEventIfPresent() {
        User user = new User();
        user.setId(1L);
        Petition petition = new Petition();
        petition.setId(2L);
        String eventId = "e1";
        String reminderId = "r1";

        UserPetitionEventId id = new UserPetitionEventId(user.getId(), petition.getId());
        UserPetitionEvent existingEvent = new UserPetitionEvent();
        existingEvent.setId(id);
        existingEvent.setUser(user);
        existingEvent.setPetition(petition);
        existingEvent.setEventId(eventId);
        existingEvent.setReminderEventId(reminderId);

        when(userPetitionEventRepository.findById(id)).thenReturn(Optional.of(existingEvent));

        UserPetitionEvent result = service.create(user, petition, eventId, reminderId);

        assertEquals(existingEvent, result);
        verify(userPetitionEventRepository).findById(id);
        verify(userPetitionEventRepository, never()).save(any());
    }

    @Test
    void create_shouldSaveAndReturnNewEventIfNotExists() {
        User user = new User();
        user.setId(1L);
        Petition petition = new Petition();
        petition.setId(2L);
        String eventId = "e1";
        String reminderId = "r1";

        UserPetitionEventId id = new UserPetitionEventId(user.getId(), petition.getId());

        when(userPetitionEventRepository.findById(id)).thenReturn(Optional.empty());

        UserPetitionEvent savedEvent = new UserPetitionEvent();
        savedEvent.setId(id);
        savedEvent.setUser(user);
        savedEvent.setPetition(petition);
        savedEvent.setEventId(eventId);
        savedEvent.setReminderEventId(reminderId);

        when(userPetitionEventRepository.save(any(UserPetitionEvent.class))).thenReturn(savedEvent);

        UserPetitionEvent result = service.create(user, petition, eventId, reminderId);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(petition, result.getPetition());
        assertEquals(eventId, result.getEventId());
        assertEquals(reminderId, result.getReminderEventId());

        verify(userPetitionEventRepository).findById(id);
        verify(userPetitionEventRepository).save(any(UserPetitionEvent.class));
    }

    @Test
    void findByUserAndPetition_shouldReturnEventIfFound() {
        long userId = 1L;
        long petitionId = 2L;

        UserPetitionEventId id = new UserPetitionEventId(userId, petitionId);
        UserPetitionEvent event = new UserPetitionEvent();
        event.setId(id);

        when(userPetitionEventRepository.findById_UserIdAndId_PetitionId(userId, petitionId)).thenReturn(Optional.of(event));

        UserPetitionEvent result = service.findByUserAndPetition(userId, petitionId);

        assertEquals(event, result);
        verify(userPetitionEventRepository).findById_UserIdAndId_PetitionId(userId, petitionId);
    }

    @Test
    void findByUserAndPetition_shouldThrowIfNotFound() {
        long userId = 1L;
        long petitionId = 2L;

        when(userPetitionEventRepository.findById_UserIdAndId_PetitionId(userId, petitionId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByUserAndPetition(userId, petitionId));

        assertTrue(ex.getMessage().contains("UserPetitionEvent with id"));
        verify(userPetitionEventRepository).findById_UserIdAndId_PetitionId(userId, petitionId);
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        long userId = 1L;
        long petitionId = 2L;

        UserPetitionEventId id = new UserPetitionEventId(userId, petitionId);

        doNothing().when(userPetitionEventRepository).deleteById(id);

        service.delete(userId, petitionId);

        verify(userPetitionEventRepository).deleteById(id);
    }

    @Test
    void findAllByUser_shouldReturnList() {
        long userId = 1L;
        List<UserPetitionEvent> list = List.of(new UserPetitionEvent());

        when(userPetitionEventRepository.findAllById_UserId(userId)).thenReturn(list);

        List<UserPetitionEvent> result = service.findAllByUser(userId);

        assertEquals(list, result);
        verify(userPetitionEventRepository).findAllById_UserId(userId);
    }

    @Test
    void findAllByPetition_shouldReturnList() {
        long petitionId = 2L;
        List<UserPetitionEvent> list = List.of(new UserPetitionEvent());

        when(userPetitionEventRepository.findAllById_PetitionId(petitionId)).thenReturn(list);

        List<UserPetitionEvent> result = service.findAllByPetition(petitionId);

        assertEquals(list, result);
        verify(userPetitionEventRepository).findAllById_PetitionId(petitionId);
    }

    @Test
    void deleteByUser_shouldCallDeleteAllByUserId() {
        long userId = 1L;

        doNothing().when(userPetitionEventRepository).deleteAllByUser_Id(userId);

        service.deleteByUser(userId);

        verify(userPetitionEventRepository).deleteAllByUser_Id(userId);
    }
}

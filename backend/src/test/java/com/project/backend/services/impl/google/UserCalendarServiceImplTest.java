package com.project.backend.services.impl.google;

import com.project.backend.models.google.UserCalendar;
import com.project.backend.repositories.repos.google.UserCalendarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCalendarServiceImplTest {

    @Mock
    private UserCalendarRepository userCalendarRepository;

    @InjectMocks
    private UserCalendarServiceImpl userCalendarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAndReturnUserCalendar() {
        UserCalendar userCalendar = new UserCalendar();
        userCalendar.setCalendarId("cal123");

        when(userCalendarRepository.save(userCalendar)).thenReturn(userCalendar);

        UserCalendar result = userCalendarService.create(userCalendar);

        assertEquals(userCalendar, result);
        verify(userCalendarRepository).save(userCalendar);
    }

    @Test
    void findByUser_shouldReturnUserCalendarIfExists() {
        long userId = 1L;
        UserCalendar userCalendar = new UserCalendar();
        userCalendar.setCalendarId("cal123");

        when(userCalendarRepository.findByUser_Id(userId)).thenReturn(Optional.of(userCalendar));

        UserCalendar result = userCalendarService.findByUser(userId);

        assertEquals(userCalendar, result);
        verify(userCalendarRepository).findByUser_Id(userId);
    }

    @Test
    void findByUser_shouldThrowEntityNotFoundExceptionIfNotFound() {
        long userId = 1L;

        when(userCalendarRepository.findByUser_Id(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userCalendarService.findByUser(userId));

        assertTrue(exception.getMessage().contains("UserCalendar with userId " + userId + " not found"));
        verify(userCalendarRepository).findByUser_Id(userId);
    }

    @Test
    void existsByUser_shouldReturnTrueIfExists() {
        long userId = 1L;

        when(userCalendarRepository.existsByUser_Id(userId)).thenReturn(true);

        boolean exists = userCalendarService.existsByUser(userId);

        assertTrue(exists);
        verify(userCalendarRepository).existsByUser_Id(userId);
    }

    @Test
    void existsByUser_shouldReturnFalseIfNotExists() {
        long userId = 1L;

        when(userCalendarRepository.existsByUser_Id(userId)).thenReturn(false);

        boolean exists = userCalendarService.existsByUser(userId);

        assertFalse(exists);
        verify(userCalendarRepository).existsByUser_Id(userId);
    }

    @Test
    void deleteByUser_shouldCallRepositoryDelete() {
        long userId = 1L;

        doNothing().when(userCalendarRepository).deleteByUser_Id(userId);

        userCalendarService.deleteByUser(userId);

        verify(userCalendarRepository).deleteByUser_Id(userId);
    }
}

package com.project.backend.services.impl;

import com.project.backend.models.School;
import com.project.backend.repositories.repos.SchoolRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SchoolServiceImplTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private SchoolServiceImpl schoolService;

    private School testSchool;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testSchool = new School();
        testSchool.setId(1L);
        testSchool.setName("Test School");
    }

    @Test
    void findById_shouldReturnSchool_whenExists() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(testSchool));

        School result = schoolService.findById(1L);

        assertEquals(testSchool, result);
        verify(schoolRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            schoolService.findById(1L);
        });

        assertEquals("School not found with id 1", exception.getMessage());
        verify(schoolRepository).findById(1L);
    }

    @Test
    void save_shouldReturnSavedSchool() {
        when(schoolRepository.save(testSchool)).thenReturn(testSchool);

        School result = schoolService.save(testSchool);

        assertEquals(testSchool, result);
        verify(schoolRepository).save(testSchool);
    }

    @Test
    void update_shouldUpdateAndReturnSchool_whenExists() {
        School updatedData = new School();
        updatedData.setName("Updated School");

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(testSchool));
        when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> invocation.getArgument(0));

        School result = schoolService.update(updatedData, 1L);

        assertEquals("Updated School", result.getName());
        verify(schoolRepository).findById(1L);
        verify(schoolRepository).save(testSchool);
    }

    @Test
    void update_shouldThrowException_whenSchoolNotFound() {
        School updatedData = new School();
        updatedData.setName("Updated School");

        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            schoolService.update(updatedData, 1L);
        });

        verify(schoolRepository).findById(1L);
        verify(schoolRepository, never()).save(any());
    }

    @Test
    void delete_shouldCallDeleteById_whenSchoolExists() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(testSchool));
        doNothing().when(schoolRepository).deleteById(1L);

        schoolService.delete(1L);

        verify(schoolRepository).findById(1L);
        verify(schoolRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenSchoolNotFound() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            schoolService.delete(1L);
        });

        verify(schoolRepository).findById(1L);
        verify(schoolRepository, never()).deleteById(anyLong());
    }
}

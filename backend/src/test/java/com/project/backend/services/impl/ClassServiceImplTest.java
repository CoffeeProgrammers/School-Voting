package com.project.backend.services.impl;

import com.project.backend.models.Class;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.repositories.repos.ClassRepository;
import com.project.backend.services.inter.SchoolService;
import com.project.backend.services.inter.UserDeletionService;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassServiceImplTest {

    @Mock private UserDeletionService userDeletionService;
    @Mock private ClassRepository classRepository;
    @Mock private UserService userService;
    @Mock private SchoolService schoolService;
    @Mock private PetitionService petitionService;
    @Mock private VotingService votingService;

    @InjectMocks
    private ClassServiceImpl classService;

    @Test
    void testCreate() {
        Class clazz = new Class();
        clazz.setName("10-А");

        School school = new School();
        school.setId(1L);

        User user = new User();
        user.setId(2L);

        when(userService.findById(2L)).thenReturn(user);
        when(schoolService.findById(1L)).thenReturn(school);
        when(classRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Class result = classService.create(clazz, List.of(2L), 1L);

        assertEquals("10-А", result.getName());
        assertEquals(school, result.getSchool());
        verify(userService).save(user);
    }

    @Test
    void testUpdate() {
        Class existing = new Class();
        existing.setId(1L);
        existing.setName("Old");

        when(classRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(classRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Class newData = new Class();
        newData.setName("New");

        Class updated = classService.update(newData, 1L);
        assertEquals("New", updated.getName());
    }

    @Test
    void testDeleteBySchool() {
        Class clazz = new Class();
        clazz.setId(1L);
        clazz.setUsers(new HashSet<>());
        when(classRepository.findAll((Specification<Class>) any())).thenReturn(List.of(clazz));
        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));

        doNothing().when(classRepository).deleteById(1L);

        classService.deleteBySchool(1L);

        verify(classRepository).deleteById(1L);
    }


    @Test
    void testDelete_withUsers() {
        Class clazz = new Class();
        clazz.setId(1L);

        User user = new User();
        user.setId(2L);

        clazz.setUsers(Set.of(user));

        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));
        doNothing().when(classRepository).deleteById(1L);

        classService.delete(1L, true);
        verify(userDeletionService).delete(user, false);
        verify(classRepository).deleteById(1L);
    }

    @Test
    void testAssignUserToClass() {
        Class clazz = new Class();
        clazz.setId(1L);
        clazz.setUsers(new HashSet<>());

        User user = new User();
        user.setId(2L);
        user.setRole("STUDENT");

        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));
        when(userService.findById(2L)).thenReturn(user);

        classService.assignUserToClass(1L, List.of(2L));

        assertTrue(clazz.getUsers().contains(user));
        verify(userService).assignClassToUser(clazz, user);
    }

    @Test
    void testUnassignUserFromClass() {
        Class clazz = new Class();
        clazz.setId(1L);
        User user = new User();
        user.setId(2L);
        user.setRole("STUDENT");

        clazz.setUsers(new HashSet<>(Set.of(user)));

        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));
        when(userService.findById(2L)).thenReturn(user);

        classService.unassignUserFromClass(1L, List.of(2L));

        assertFalse(clazz.getUsers().contains(user));
        verify(userService).unassignClassFromUser(user);
    }

    @Test
    void testFindById_success() {
        Class clazz = new Class();
        clazz.setId(1L);

        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));

        Class found = classService.findById(1L);
        assertEquals(1L, found.getId());
    }

    @Test
    void testFindById_notFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> classService.findById(1L));
    }

    @Test
    void testFindAllBySchool_withName() {
        when(classRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(Page.empty());

        Page<Class> page = classService.findAllBySchool(1L, "10-А", 0, 10);
        assertNotNull(page);
    }

    @Test
    void testFindByUser() {
        User user = new User();
        user.setId(2L);

        Class clazz = new Class();
        clazz.setId(1L);

        when(classRepository.findAll((Specification<Class>)any())).thenReturn(List.of(clazz));

        Class found = classService.findByUser(user);
        assertEquals(1L, found.getId());
    }

    @Test
    void testFindByUser_noClass() {
        when(classRepository.findAll((Specification<Class>) any())).thenReturn(List.of());

        User user = new User();
        Class found = classService.findByUser(user);
        assertNull(found);
    }
}

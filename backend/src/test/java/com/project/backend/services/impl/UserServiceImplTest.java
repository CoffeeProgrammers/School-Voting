package com.project.backend.services.impl;

import com.project.backend.TestUtil;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.repos.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock private UserRepository userRepository;
    @Mock private SchoolServiceImpl schoolService;
    @InjectMocks private UserServiceImpl userService;

    private User user;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        user = TestUtil.createUser("TEACHER", "some@gmail.com");
        updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setFirstName(user.getFirstName() + "SD");
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setRole(user.getRole());
        updatedUser.setSchool(user.getSchool());
        updatedUser.setMyClass(user.getMyClass());
        updatedUser.setKeycloakUserId(user.getKeycloakUserId());
    }

    private Page<User> createPage(List<User> users) {
        return new PageImpl<>(users);
    }

    @Test
    void create_success() {
        when(userRepository.save(user)).thenReturn(user);
        when(schoolService.findById(user.getSchool().getId())).thenReturn(user.getSchool());

        User result = userService.createUserKeycloak(user, user.getSchool().getId());

        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    void update_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);

        user.setFirstName(updatedUser.getFirstName());
        userService.updateUserKeycloak(user, user.getId());

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(user);
    }

    @Test
    void updateDeleted_unsuccess() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        user.setFirstName(updatedUser.getFirstName());
        user.setEmail("!deleted-user!@deleted.com");
        updatedUser.setEmail("!deleted-user!@deleted.com");

        assertThrows(EntityExistsException.class, () -> userService.updateUserKeycloak(user, user.getId()));

        verify(userRepository).findById(user.getId());
    }

    @Test
    void findById_unsuccess() {
        long nonExistentId = user.getId() + 1;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(nonExistentId));
        verify(userRepository).findById(nonExistentId);
    }

    @Test
    void findAllBySchool_variants() {
        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user, updatedUser));

        // findAllBySchool без виключення
        List<User> all = userService.findAllBySchool(user.getSchool().getId());
        assertEquals(2, all.size());

        // findAllBySchool з виключенням по userId
        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(updatedUser));
        List<User> excluded = userService.findAllBySchool(user.getSchool().getId(), user.getId());
        assertEquals(1, excluded.size());

        verify(userRepository, times(2)).findAll(any(Specification.class));
    }

    @Test
    void findAllByClass_variants() {
        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user, updatedUser));

        List<User> all = userService.findAllByClass(user.getMyClass().getId());
        assertEquals(2, all.size());

        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(updatedUser));
        List<User> excluded = userService.findAllByClass(user.getMyClass().getId(), user.getId());
        assertEquals(1, excluded.size());

        verify(userRepository, times(2)).findAll(any(Specification.class));
    }

    @Test
    void findUserByAuth_success() {
        Authentication auth = mock(Authentication.class);
        String keycloakUserId = "keycloak-id-123";

        when(auth.getName()).thenReturn(keycloakUserId);
        when(userRepository.findByKeycloakUserId(keycloakUserId)).thenReturn(Optional.of(user));

        assertEquals(user, userService.findUserByAuth(auth));
        verify(auth).getName();
        verify(userRepository).findByKeycloakUserId(keycloakUserId);
    }

    @Test
    void findAllByPetition_variousLevels() {
        Petition petition = mock(Petition.class);
        User creator = mock(User.class);
        School school = mock(School.class);

        // SCHOOL level
        when(petition.getLevelType()).thenReturn(LevelType.SCHOOL);
        when(petition.getCreator()).thenReturn(creator);
        when(creator.getSchool()).thenReturn(school);
        when(school.getId()).thenReturn(1L);
        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user, updatedUser));

        List<User> schoolUsers = userService.findAllByPetition(petition);
        assertEquals(2, schoolUsers.size());

        // CLASS level
        when(petition.getLevelType()).thenReturn(LevelType.CLASS);
        when(petition.getTargetId()).thenReturn(5L);
        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user));

        List<User> classUsers = userService.findAllByPetition(petition);
        assertEquals(1, classUsers.size());

        // Other level returns empty
        when(petition.getLevelType()).thenReturn(LevelType.GROUP_OF_TEACHERS);
        List<User> others = userService.findAllByPetition(petition);
        assertTrue(others.isEmpty());

        verify(userRepository, times(2)).findAll(any(Specification.class));
    }

    @Test
    void findAllByVoting_success() {
        Voting voting = mock(Voting.class);
        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user, updatedUser));

        List<User> result = userService.findAllByVoting(voting);

        assertEquals(2, result.size());
        verify(userRepository).findAll(any(Specification.class));
    }

    @Test
    void findByKeycloakUserId_variants() {
        when(userRepository.findByKeycloakUserId(user.getKeycloakUserId())).thenReturn(Optional.of(user));
        User found = userService.findUserByKeycloakUserId(user.getKeycloakUserId());
        assertEquals(user, found);

        when(userRepository.findByKeycloakUserId(user.getKeycloakUserId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> userService.findUserByKeycloakUserId(user.getKeycloakUserId()));

        verify(userRepository, times(2)).findByKeycloakUserId(user.getKeycloakUserId());
    }

    @Test
    void findUserByEmail_variants() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User found = userService.findUserByEmail(user.getEmail());
        assertEquals(user, found);

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> userService.findUserByEmail("nonexistent@example.com"));

        verify(userRepository, times(2)).findByEmail(anyString());
    }

    @Test
    void paginationMethods_variants() {
        Page<User> page = createPage(List.of(user, updatedUser));
        when(userRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        // findAllByVotingPage
        Page<User> votingPage = userService.findAllByVoting(1L, "email", "fn", "ln", 0, 10);
        assertEquals(page, votingPage);

        // findAllByRoleInSchool with createSpecification null or non-null
        Page<User> rolePage1 = userService.findAllByRoleInSchool(1L, "TEACHER", null, null, null, 0, 10, -1);
        Page<User> rolePage2 = userService.findAllByRoleInSchool(1L, "TEACHER", "email@example.com", null, null, 0, 10, -1);
        assertEquals(page, rolePage1);
        assertEquals(page, rolePage2);

        // findAllByClass with createSpecification null or non-null
        Page<User> classPage1 = userService.findAllByClass(2L, null, null, null, 0, 10);
        Page<User> classPage2 = userService.findAllByClass(2L, "email@example.com", null, null, 0, 10);
        assertEquals(page, classPage1);
        assertEquals(page, classPage2);

        // findAllStudentsWithoutClass with createSpecification null or non-null
        Page<User> noClass1 = userService.findAllStudentsWithoutClass(1L, null, null, null, 0, 10);
        Page<User> noClass2 = userService.findAllStudentsWithoutClass(1L, "email@example.com", null, null, 0, 10);
        assertEquals(page, noClass1);
        assertEquals(page, noClass2);

        verify(userRepository, atLeast(7)).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void checkEmail_variants() {
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        assertDoesNotThrow(() -> userService.checkEmail("newuser@example.com"));

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        EntityExistsException ex = assertThrows(EntityExistsException.class, () -> userService.checkEmail(user.getEmail()));
        assertEquals("User with email " + user.getEmail() + " already exists", ex.getMessage());

        verify(userRepository, times(2)).existsByEmail(anyString());
    }

    @Test
    void isNotExistByEmail_variants() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        assertTrue(userService.isNotExistByEmail("test@example.com"));

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
        assertFalse(userService.isNotExistByEmail("existing@example.com"));

        verify(userRepository, times(2)).existsByEmail(anyString());
    }

    @Test
    void countMethods() {
        when(userRepository.countAllBySchool_IdAndRoleIs(1L, "TEACHER")).thenReturn(5L);
        long schoolCount = userService.countAllBySchoolAndRole(1L, "TEACHER");
        assertEquals(5L, schoolCount);

        when(userRepository.countAllByMyClass_Id(user.getMyClass().getId())).thenReturn(1L);
        long classCount = userService.countAllByClass(user.getMyClass().getId());
        assertEquals(1L, classCount);

        verify(userRepository).countAllBySchool_IdAndRoleIs(1L, "TEACHER");
        verify(userRepository).countAllByMyClass_Id(user.getMyClass().getId());
    }

    @Test
    void save_assignUnassignClass() {
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.save(user);
        assertEquals(user, saved);

        userService.assignClassToUser(user.getMyClass(), user);
        userService.unassignClassFromUser(user);

        verify(userRepository, times(3)).save(user);
    }
}

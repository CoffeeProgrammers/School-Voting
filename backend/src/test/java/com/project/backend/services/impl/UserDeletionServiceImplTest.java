package com.project.backend.services.impl;

import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.google.GoogleCalendarCredential;
import com.project.backend.repositories.repos.UserRepository;
import com.project.backend.services.inter.SchoolService;
import com.project.backend.services.inter.google.GoogleCalendarService;
import com.project.backend.services.inter.petition.CommentService;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import com.project.backend.services.inter.voting.VotingUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserDeletionServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private GoogleCalendarService googleCalendarService;
    @Mock private RealmResource realmResource;
    @Mock private UsersResource usersResource;
    @Mock private CommentService commentService;
    @Mock private PetitionService petitionService;
    @Mock private VotingService votingService;
    @Mock private VotingUserService votingUserService;
    @Mock private SchoolService schoolService;

    @InjectMocks
    private UserDeletionServiceImpl userDeletionService;

    private User user;
    private School school;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRole("STUDENT");
        user.setKeycloakUserId("keycloak-123");
        user.setGoogleCalendarCredential(new GoogleCalendarCredential());

        school = new School();
        school.setId(100L);
        user.setSchool(school);

        when(realmResource.users()).thenReturn(usersResource);
    }

    @Test
    void delete_shouldThrowIfUserIsDirectorAndFlagFalse() {
        user.setRole("DIRECTOR");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userDeletionService.delete(user, false);
        });

        assertEquals("Cannot delete director", ex.getMessage());
        verifyNoInteractions(googleCalendarService, commentService, petitionService);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void delete_shouldDeleteDirectorIfAllowed() {
        user.setRole("DIRECTOR");

        userDeletionService.delete(user, true);

        verify(schoolService).save(school);
        verify(usersResource).delete("keycloak-123");
        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowIfUserAlreadyDeleted() {
        user.setRole("DELETED");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userDeletionService.delete(user, true);
        });

        assertEquals("Cannot delete deleted", ex.getMessage());
        verifyNoInteractions(googleCalendarService, commentService, petitionService);
    }

    @Test
    void delete_shouldSkipGoogleCalendarIfNull() {
        user.setGoogleCalendarCredential(null);

        userDeletionService.delete(user, false);

        verify(googleCalendarService, never()).deleteCalendarAndRevoke(any());
        verify(commentService).deleteingUser(1L);
        verify(petitionService).deletingUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteAllBySchool_shouldDeleteAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setRole("STUDENT");
        user1.setKeycloakUserId("kc1");
        user1.setSchool(school);

        User user2 = new User();
        user2.setId(2L);
        user2.setRole("STUDENT");
        user2.setKeycloakUserId("kc2");
        user2.setSchool(school);

        when(userRepository.findAll((Specification<User>) any())).thenReturn(List.of(user1, user2));
        when(realmResource.users()).thenReturn(usersResource);

        userDeletionService.deleteAllBySchool(100L);

        verify(userRepository, times(2)).deleteById(anyLong());
        verify(usersResource).delete("kc1");
        verify(usersResource).delete("kc2");
    }
}

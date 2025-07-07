package com.project.backend.auth.config;

import com.project.backend.models.Class;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.petition.Comment;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.petition.CommentService;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import com.project.backend.services.inter.voting.VotingUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserSecurityTest {

    private UserService userService;
    private PetitionService petitionService;
    private CommentService commentService;
    private VotingService votingService;
    private VotingUserService votingUserService;
    private UserSecurity userSecurity;

    private Authentication authentication;
    private final String userKeycloakId = "abc-123";
    private final long userId = 1L;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        petitionService = mock(PetitionService.class);
        commentService = mock(CommentService.class);
        votingService = mock(VotingService.class);
        votingUserService = mock(VotingUserService.class);

        userSecurity = new UserSecurity(userService, petitionService, commentService, votingService, votingUserService);

        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userKeycloakId);
    }

    @Test
    void checkUser_shouldReturnTrueWhenIdsMatch() {
        User user = new User();
        user.setKeycloakUserId(userKeycloakId);
        when(userService.findById(userId)).thenReturn(user);

        assertTrue(userSecurity.checkUser(authentication, userId));
    }

    @Test
    void checkUserClass_shouldReturnTrueIfUserInClass() {
        User user = new User();
        Class myClass = new Class();
        myClass.setId(10L);
        user.setMyClass(myClass);

        when(userService.findUserByAuth(authentication)).thenReturn(user);

        assertTrue(userSecurity.checkUserClass(authentication, 10L));
    }

    @Test
    void checkUserSchool_shouldReturnTrueIfUserInSchool() {
        User user = new User();
        School school = new School();
        school.setId(20L);
        user.setSchool(school);

        when(userService.findUserByAuth(authentication)).thenReturn(user);

        assertTrue(userSecurity.checkUserSchool(authentication, 20L));
    }

    @Test
    void checkCreatorPetition_shouldReturnTrueWhenUserIsCreator() {
        Petition petition = new Petition();
        User creator = new User();
        creator.setKeycloakUserId(userKeycloakId);
        petition.setCreator(creator);

        when(petitionService.findById(5L)).thenReturn(petition);

        assertTrue(userSecurity.checkCreatorPetition(authentication, 5L));
    }

    @Test
    void checkCreatorVoting_shouldReturnTrueWhenUserIsCreator() {
        Voting voting = new Voting();
        User creator = new User();
        creator.setKeycloakUserId(userKeycloakId);
        voting.setCreator(creator);

        when(votingService.findById(6L)).thenReturn(voting);

        assertTrue(userSecurity.checkCreatorVoting(authentication, 6L));
    }

    @Test
    void checkCreatorComment_shouldReturnTrueWhenUserIsCreator() {
        Comment comment = new Comment();
        User creator = new User();
        creator.setKeycloakUserId(userKeycloakId);
        comment.setCreator(creator);

        when(commentService.findById(7L)).thenReturn(comment);

        assertTrue(userSecurity.checkCreatorComment(authentication, 7L));
    }

    @Test
    void checkUserPetition_shouldReturnTrueForSchoolLevel() {
        Petition petition = new Petition();
        petition.setLevelType(LevelType.SCHOOL);

        User creator = new User();
        School school = new School();
        school.setId(1L);
        creator.setSchool(school);
        petition.setCreator(creator);

        User user = new User();
        user.setSchool(school);

        when(petitionService.findById(8L)).thenReturn(petition);
        when(userService.findUserByAuth(authentication)).thenReturn(user);

        assertTrue(userSecurity.checkUserPetition(authentication, 8L));
    }

    @Test
    void checkUserPetition_shouldReturnTrueForClassLevel() {
        Petition petition = new Petition();
        petition.setLevelType(LevelType.CLASS);
        petition.setTargetId(10L);

        User user = new User();
        when(petitionService.findById(9L)).thenReturn(petition);
        when(userService.findUserByAuth(authentication)).thenReturn(user);
        when(userService.findAllByClass(10L)).thenReturn(List.of(user));

        assertTrue(userSecurity.checkUserPetition(authentication, 9L));
    }

    @Test
    void checkUserVoting_shouldReturnTrueWhenUserInVoting() {
        User user = new User();
        user.setId(42L);
        when(userService.findUserByAuth(authentication)).thenReturn(user);
        when(votingUserService.existsById(3L, 42L)).thenReturn(true);

        assertTrue(userSecurity.checkUserVoting(authentication, 3L));
    }
}

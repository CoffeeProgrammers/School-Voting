package com.project.backend.auth.config;

import com.project.backend.models.School;
import com.project.backend.services.inter.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final UserService userService;
    private final PetitionService petitionService;
    private final CommentService commentService;
    private final VotingService votingService;
    private final SchoolService schoolService;
    private final VotingUserService votingUserService;

    private boolean checkUser(Authentication authentication, String username) {
        log.info("preAuth: Checking user {}", username);
        String authenticatedUserEmail = userService.findUserByAuth(authentication).getEmail();
        return authenticatedUserEmail.equals(username);
    }

    public boolean checkUserSchool(Authentication authentication, long schoolId) {
        log.info("preAuth: Checking if user in school {}", schoolId);
        School school = userService.findUserByAuth(authentication).getSchool();
        return school.getId() == schoolId;
    }

    public boolean checkDirectorOfSchool(Authentication authentication, long schoolId) {
        log.info("preAuth: Checking if director of school {}", schoolId);
        return schoolService.findById(schoolId).getDirector().getKeycloakUserId()
                .equals(authentication.getName());
    }

    public boolean checkCreatorPetition(Authentication authentication, long petitionId) {
        log.info("preAuth: Checking if creator of petition {}", petitionId);
        return  petitionService.findById(petitionId).getCreator().getKeycloakUserId()
                .equals(authentication.getName());
    }

    public boolean checkCreatorVoting(Authentication authentication, long votingId) {
        log.info("preAuth: Checking if creator of voting {}", votingId);
        return  votingService.findById(votingId).getCreator().getKeycloakUserId()
                .equals(authentication.getName());
    }

    public boolean checkCreatorComment(Authentication authentication, long commentId) {
        log.info("preAuth: Checking if creator of comment {}", commentId);
        return  commentService.findById(commentId).getCreator().getKeycloakUserId()
                .equals(authentication.getName());
    }

    public boolean checkUserPetition(Authentication authentication, long petitionId) {
        log.info("preAuth: Checking if user in petition {}", petitionId);
        return petitionService.findById(petitionId).getUsers().contains(userService.findUserByAuth(authentication));
    }

    public boolean checkUserVoting(Authentication authentication, long votingId) {
        log.info("preAuth: Checking if user in voting {}", votingId);
        return votingUserService.exitsById(votingId, userService.findUserByAuth(authentication).getId());
    }

}

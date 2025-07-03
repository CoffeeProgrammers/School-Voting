package com.project.backend.auth.config;

import com.project.backend.mappers.CommentMapper;
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

    private boolean checkUser(Authentication authentication, String username) {
        log.info("preAuth: Checking user {}", username);
        String authenticatedUserEmail = userService.findUserByAuth(authentication).getEmail();
        return authenticatedUserEmail.equals(username);
    }

    private boolean checkUserSchool(Authentication authentication, long schoolId) {
        log.info("preAuth: Checking if user in school {}", schoolId);
        School school = userService.findUserByAuth(authentication).getSchool();
        return school.getId() == schoolId;
    }

    private boolean checkDirectorOfSchool(Authentication authentication, long schoolId) {
        log.info("preAuth: Checking if director of school {}", schoolId);
        return schoolService.findById(schoolId).getDirector().getEmail()
                .equals(userService.findUserByAuth(authentication).getEmail());
    }

    private boolean checkCreatorPetition(Authentication authentication, long petitionId) {
        log.info("preAuth: Checking if creator of petition {}", petitionId);
        return  petitionService.findById(petitionId).getCreator().getEmail()
                .equals(userService.findUserByAuth(authentication).getEmail());
    }

    private boolean checkCreatorVoting(Authentication authentication, long votingId) {
        log.info("preAuth: Checking if creator of voting {}", votingId);
        return  votingService.findById(votingId).getCreator().getEmail()
                .equals(userService.findUserByAuth(authentication).getEmail());
    }

    private boolean checkCreatorComment(Authentication authentication, long commentId) {
        log.info("preAuth: Checking if creator of comment {}", commentId);
        return  commentService.findById(commentId).getCreator().getEmail()
                .equals(userService.findUserByAuth(authentication).getEmail());
    }

    private boolean checkUserPetition(Authentication authentication, long petitionId) {
        log.info("preAuth: Checking if user in petition {}", petitionId);
        return petitionService.findById(petitionId).getUsers().contains(userService.findUserByAuth(authentication));
    }


}

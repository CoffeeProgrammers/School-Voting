package com.project.backend.auth.config;

import com.project.backend.models.School;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.petition.Petition;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.petition.CommentService;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import com.project.backend.services.inter.voting.VotingUserService;
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
    private final VotingUserService votingUserService;

    public boolean checkUser(Authentication authentication, long userId) {
        log.info("preAuth: Checking user {}", userId);
        return userService.findById(userId).getKeycloakUserId().equals(authentication.getName());
    }

    public boolean checkUserClass(Authentication authentication, long classId) {
        log.info("preAuth: Checking if user in class {}", classId);
        return userService.findUserByAuth(authentication).getMyClass().getId() == classId;
    }

    public boolean checkUserSchool(Authentication authentication, long schoolId) {
        log.info("preAuth: Checking if user in school {}", schoolId);
        School school = userService.findUserByAuth(authentication).getSchool();
        return school.getId() == schoolId;
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

    public boolean checkUserInSameSchoolAsVoting(Authentication authentication, long votingId) {
        log.info("preAuth: Checking if user in the same school as voting {}", votingId);
        return votingService.findById(votingId).getSchool().getId() == userService.findUserByAuth(authentication).getSchool().getId();
    }

    public boolean checkUserPetition(Authentication authentication, long petitionId) {
        log.info("preAuth: Checking if user in petition {}", petitionId);
        Petition petition = petitionService.findById(petitionId);
        if(petition.getLevelType().equals(LevelType.SCHOOL)){
            log.info("preAuth: Checking if user in school");
            return checkUserSchool(authentication, petition.getTargetId());
        }else{
            log.info("preAuth: Checking if user in class");
            return userService.findAllByClass(petition.getTargetId()).contains(userService.findUserByAuth(authentication));
        }
    }

    public boolean checkUserVoting(Authentication authentication, long votingId) {
        log.info("preAuth: Checking if user in voting {}", votingId);
        return votingUserService.existsById(votingId, userService.findUserByAuth(authentication).getId());
    }

}

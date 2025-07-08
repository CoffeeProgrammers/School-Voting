package com.project.backend.services.impl;

import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.petition.Petition;
import com.project.backend.repositories.repos.UserRepository;
import com.project.backend.repositories.specification.UserSpecification;
import com.project.backend.services.inter.SchoolService;
import com.project.backend.services.inter.UserDeletionService;
import com.project.backend.services.inter.google.GoogleCalendarService;
import com.project.backend.services.inter.petition.CommentService;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import com.project.backend.services.inter.voting.VotingUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDeletionServiceImpl implements UserDeletionService {

    private final UserRepository userRepository;
    private final GoogleCalendarService googleCalendarService;
    private final RealmResource realmResource;
    private final CommentService commentService;
    private final PetitionService petitionService;
    private final VotingService votingService;
    private final VotingUserService votingUserService;
    private final SchoolService schoolService;

    @Override
    public void delete(User user, boolean isDeleteDirector) {
        log.info("Service: Deleting User with id {}", user.getId());
        long userId = user.getId();

        if (!isDeleteDirector) {
            if("DIRECTOR".equals(user.getRole())) {
                throw new IllegalArgumentException("Cannot delete director");
            }
        } else {
            School school = user.getSchool();
            school.setDirector(null);
            schoolService.save(school);
        }
        if ("DELETED".equals(user.getRole())) {
            throw new IllegalArgumentException("Cannot delete deleted");
        }

        if (user.getGoogleCalendarCredential() != null) {
            googleCalendarService.deleteCalendarAndRevoke(user);
        }

        commentService.deleteingUser(userId);
        petitionService.deletingUser(userId);
        votingService.deletingUser(userId);
        votingUserService.deleteWithUser(userId);

        realmResource.users().delete(user.getKeycloakUserId());
        List<Petition> petitions = petitionService.findAllMy(userId);
        userRepository.deleteById(user.getId());

        for(Petition petition : petitions) {
            petitionService.checkingStatus(petition);
            petitionService.save(petition);
        }

        log.info("Service: Deleted user with id {}", userId);
    }

    @Override
    public void deleteAllBySchool(long schoolId) {
        log.info("Service: Deleting all users fromm school with id {}", schoolId);
        List<User> list = userRepository.findAll(UserSpecification.bySchool(schoolId));
        for (User user : list) {
            delete(user, true);
        }
    }
}
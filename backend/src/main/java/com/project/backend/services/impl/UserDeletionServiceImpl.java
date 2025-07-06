package com.project.backend.services.impl;

import com.project.backend.models.User;
import com.project.backend.repositories.repos.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void delete(User user, boolean isDeleteDirector) {
        log.info("Service: Deleting Classes with id {}", user.getId());
        long userId = user.getId();

        if (!isDeleteDirector && "DIRECTOR".equals(user.getRole())) {
            throw new IllegalArgumentException("Cannot delete director");
        }
        if ("DELETED".equals(user.getRole())) {
            throw new IllegalArgumentException("Cannot delete deleted");
        }

        googleCalendarService.deleteCalendarAndRevoke(user);

        commentService.deleteingUser(userId);
        petitionService.deletingUser(userId);
        votingService.deletingUser(userId);
        votingUserService.deleteWithUser(userId);

        realmResource.users().delete(user.getKeycloakUserId());

        userRepository.delete(user);

        log.info("Service: Deleted user with id {}", userId);
    }

}

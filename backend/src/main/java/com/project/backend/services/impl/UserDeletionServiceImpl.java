package com.project.backend.services.impl;

import com.project.backend.models.User;
import com.project.backend.repositories.repos.UserRepository;
import com.project.backend.repositories.specification.UserSpecification;
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

    @Transactional
    @Override
    public void delete(User user, boolean isDeleteDirector) {
        long userId = user.getId();

        googleCalendarService.deleteCalendarAndRevoke(user);
        commentService.deleteingUser(userId);
        petitionService.deletingUser(userId);
        votingService.deletingUser(userId);
        votingUserService.deleteWithUser(userId);
        log.info("Service: Deleting user with id {}", user.getId());
        if (!isDeleteDirector && user.getRole().equals("DIRECTOR")) {
            throw new IllegalArgumentException("Cannot delete director");
        }
        if (user.getRole().equals("DELETED")) {
            throw new IllegalArgumentException("Cannot delete deleted");
        }
        user.setSchool(null);
        user.setMyClass(null);
        user.setPetitions(null);
        user.setVotingUsers(null);
        userRepository.save(user);

        realmResource.users().delete(user.getKeycloakUserId());
        userRepository.deleteById(user.getId());
    }

    @Transactional
    @Override
    public void deleteBySchool(long schoolId) {
        List<User> users = userRepository.findAll(UserSpecification.bySchool(schoolId));
        users.forEach(u -> delete(u, true));
    }
}

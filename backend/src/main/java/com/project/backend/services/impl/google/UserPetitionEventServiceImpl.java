package com.project.backend.services.impl.google;

import com.project.backend.models.User;
import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.google.UserPetitionEvent.UserPetitionEventId;
import com.project.backend.models.petitions.Petition;
import com.project.backend.repositories.repos.google.UserPetitionEventRepository;
import com.project.backend.services.inter.google.UserPetitionEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPetitionEventServiceImpl implements UserPetitionEventService {

    private final UserPetitionEventRepository userPetitionEventRepository;

    @Override
    public UserPetitionEvent create(User user, Petition petition, String eventId) {
        log.info("Service: create user petition event");
        UserPetitionEventId id = new UserPetitionEventId(user.getId(), petition.getId());
        return userPetitionEventRepository.findById(id).orElseGet(() -> {
            UserPetitionEvent event = new UserPetitionEvent();
            event.setId(id);
            event.setUser(user);
            event.setPetition(petition);
            event.setEventId(eventId);
            return userPetitionEventRepository.save(event);
        });
    }

    @Override
    public UserPetitionEvent findByUserAndPetition(long userId, long petitionId) {
        log.info("Service: find user petition event by user {} and petition {}",  userId, petitionId);
        return userPetitionEventRepository.findById_UserIdAndId_PetitionId(userId, petitionId).orElseThrow(() -> new EntityNotFoundException("UserPetitionEvent with id " + userId + " : " + petitionId + " not found"));
    }

    @Override
    public void delete(long userId, long petitionId) {
        log.info("Service: delete user petition event by user {} and petition {}",  userId, petitionId);
        userPetitionEventRepository.deleteById(new UserPetitionEventId(userId, petitionId));
    }

    @Override
    public List<UserPetitionEvent> findAllByUser(long userId) {
        log.info("Service: find all user`s petition events with user {}",  userId);
        return userPetitionEventRepository.findAllById_UserId(userId);
    }

    @Override
    public List<UserPetitionEvent> findAllByPetition(long petitionId) {
        log.info("Service: find all petition events by petition id {}",  petitionId);
        return userPetitionEventRepository.findAllById_PetitionId(petitionId);
    }
}

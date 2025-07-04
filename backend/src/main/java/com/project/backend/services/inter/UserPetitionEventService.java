package com.project.backend.services.inter;

import com.project.backend.models.User;
import com.project.backend.models.UserPetitionEvent;
import com.project.backend.models.petitions.Petition;

import java.util.List;

public interface UserPetitionEventService {
    UserPetitionEvent create(User user, Petition petition, String eventId);

    UserPetitionEvent findByUserAndPetition(long userId, long petitionId);

    void delete(long userId, long votingId);

    List<UserPetitionEvent> findAllByUser(long userId);

    List<UserPetitionEvent> findAllByPetition(long petitionId);
}

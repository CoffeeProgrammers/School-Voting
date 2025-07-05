package com.project.backend.services.inter.google;

import com.project.backend.models.User;
import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.petitions.Petition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserPetitionEventService {
    UserPetitionEvent create(User user, Petition petition, String eventId, String reminderEventId);

    UserPetitionEvent findByUserAndPetition(long userId, long petitionId);

    void delete(long userId, long votingId);

    List<UserPetitionEvent> findAllByUser(long userId);

    List<UserPetitionEvent> findAllByPetition(long petitionId);

    @Transactional
    void deleteByUser(long userId);
}

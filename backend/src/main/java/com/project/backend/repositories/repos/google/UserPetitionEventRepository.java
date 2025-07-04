package com.project.backend.repositories.repos.google;

import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.google.UserPetitionEvent.UserPetitionEventId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPetitionEventRepository extends JpaRepository<UserPetitionEvent, UserPetitionEventId> {
    List<UserPetitionEvent> findAllById_UserId(long userId);
    List<UserPetitionEvent> findAllById_PetitionId(long petitionId);
    Optional<UserPetitionEvent> findById_UserIdAndId_PetitionId(long userId, long petitionId);
}

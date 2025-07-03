package com.project.backend.services.inter;

import com.project.backend.models.User;
import com.project.backend.models.petitions.Petition;
import org.springframework.data.domain.Page;

public interface PetitionService {
    Petition create(Petition petition, long levelId, User creator);

    void delete(long id);
    long support(long petitionId, User user);

    void approve(long petitionId);

    void reject(long petitionId);
    Petition findById(long id);

    Page<Petition> findAllMy(String name, String status, int page, int size, long userId);

    Page<Petition> findAllByCreator(String name, String status, int page, int size, long creatorId);

    Page<Petition> findAllForDirector(String name, String status, int page, int size);
}

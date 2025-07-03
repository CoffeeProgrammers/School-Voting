package com.project.backend.services.inter;

import com.project.backend.models.petitions.Petition;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface PetitionService {
    Petition create(Petition petition, long levelId, Authentication auth);

    void delete(long id);
    long support(long petitionId,Authentication auth);

    void approve(long petitionId);

    void reject(long petitionId);
    Petition findById(long id);
    Page<Petition> findAllMy(String name, String status, int page, int size, Authentication auth);
    Page<Petition> findAllByCreator(String name, String status, int page, int size, Authentication auth);
    Page<Petition> findAllForDirector(String name, String status, int page, int size, Authentication auth);
}

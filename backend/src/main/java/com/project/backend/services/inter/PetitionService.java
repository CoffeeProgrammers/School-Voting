package com.project.backend.services.inter;

import com.project.backend.models.petitions.Petition;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface PetitionService {
    Petition create(Petition petitionRequest);
    Petition update(Petition petitionRequest);
    void delete(long id);
    void support(long petitionId, long userId);
    void accept(long petitionId);
    void reject(long petitionId);
    Petition findById(long id);
    Page<Petition> findAllMy(String name, String status, long page, long size, Authentication auth);
    Page<Petition> findAllByCreator(String name, String status, long page, long size, Authentication auth);
    Page<Petition> findAllForDirector(String name, String status, long page, long size, Authentication auth);
}

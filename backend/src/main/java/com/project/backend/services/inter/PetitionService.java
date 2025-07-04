package com.project.backend.services.inter;

import com.project.backend.models.User;
import com.project.backend.models.petitions.Petition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PetitionService {
    Petition create(Petition petition, long levelId, User creator);

    void delete(long id);
    long support(long petitionId, User user);

    long countAll(Petition petition);

    void approve(long petitionId);

    void reject(long petitionId);
    Petition findById(long id);

    List<Petition> findAllMy(long userId);

    Page<Petition> findAllMy(String name, String status, int page, int size, long userId);

    Page<Petition> findAllByCreator(String name, String status, int page, int size, long creatorId);

    Page<Petition> findAllForDirector(String name, String status, int page, int size);

    void deletingUser(long userId);

    void deleteVoteByUser(long userId);
}

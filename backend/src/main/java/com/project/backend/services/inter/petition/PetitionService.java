package com.project.backend.services.inter.petition;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.petition.Petition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PetitionService {
    Petition create(Petition petition, long levelId, User creator);

    void delete(long id);

    void deleteBy(LevelType levelType, long targetId);

    long support(long petitionId, User user);

    void checkingStatus(Petition petition);

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

package com.project.backend.repositories.repos.petition;

import com.project.backend.models.enums.LevelType;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.petition.Petition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PetitionRepository extends JpaRepository<Petition, Long>, JpaSpecificationExecutor<Petition> {
    void deleteAllByLevelTypeAndTargetId(LevelType levelType, long targetId);
}

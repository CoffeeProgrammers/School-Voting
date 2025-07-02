package com.project.backend.repositories.petitions;

import com.project.backend.models.petitions.Petition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PetitionRepository extends JpaRepository<Petition, Long>, JpaSpecificationExecutor<Petition> {
}

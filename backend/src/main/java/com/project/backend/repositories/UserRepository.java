package com.project.backend.repositories;

import com.project.backend.models.User;
import com.project.backend.models.petitions.Petition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByKeycloakUserId(String keycloakUserId);
    long countAllByPetitionsContaining(Petition petition);
}
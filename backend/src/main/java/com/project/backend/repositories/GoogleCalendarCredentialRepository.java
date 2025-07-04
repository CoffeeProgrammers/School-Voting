package com.project.backend.repositories;

import com.project.backend.models.GoogleCalendarCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleCalendarCredentialRepository extends JpaRepository<GoogleCalendarCredential, Long> {
    Optional<GoogleCalendarCredential> findByUser_Id(long userId);
    boolean existsByUser_Id(long userId);
}

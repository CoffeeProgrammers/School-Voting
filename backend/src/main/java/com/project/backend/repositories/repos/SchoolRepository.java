package com.project.backend.repositories.repos;

import com.project.backend.models.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> { }

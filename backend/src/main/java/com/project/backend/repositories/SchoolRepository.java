package com.project.backend.repositories;

import com.project.backend.models.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> { }

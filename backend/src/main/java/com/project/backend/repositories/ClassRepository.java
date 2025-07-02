package com.project.backend.repositories;

import com.project.backend.models.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClassRepository extends JpaRepository<Class, Long>, JpaSpecificationExecutor<Class> {}

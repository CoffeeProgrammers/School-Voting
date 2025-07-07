package com.project.backend.repositories.repos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ClassRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    @BeforeEach
    void setUp() {

    }
}

package com.project.backend.repositories.repos.petition;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PetitionRepositoryTest {

    @Autowired
    private PetitionRepository petitionRepository;

    @BeforeEach
    void setUp() {

    }
}

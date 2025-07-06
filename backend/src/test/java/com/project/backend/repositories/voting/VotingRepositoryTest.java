package com.project.backend.repositories.voting;

import com.project.backend.repositories.repos.voting.VotingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class VotingRepositoryTest {

    @Autowired
    private VotingRepository votingRepository;

    @BeforeEach
    void setUp() {

    }
}

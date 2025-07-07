package com.project.backend.repositories.repos.google;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserVotingEventRepositoryTest {

    @Autowired
    private UserVotingEventRepository userVotingEventRepository;

    @BeforeEach
    void setUp() {

    }
}

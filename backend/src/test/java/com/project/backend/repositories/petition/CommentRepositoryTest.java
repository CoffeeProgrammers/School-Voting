package com.project.backend.repositories.petition;

import com.project.backend.repositories.repos.petition.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {

    }
}

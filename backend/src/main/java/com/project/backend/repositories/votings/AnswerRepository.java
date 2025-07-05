package com.project.backend.repositories.votings;

import com.project.backend.models.voting.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByVoting_Id(Long id);
}

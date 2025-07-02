package com.project.backend.services.inter;

import com.project.backend.models.voting.Answer;

import java.util.List;

public interface AnswerService {
    List<Answer> create (List<String> answerRequest, long votingId);
    List<Answer> update(List<String> newText, long votingId);
    long vote(long id);
    List<Answer> findAllByVoting(long votingId);
    Answer findById(long id);
}

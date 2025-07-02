package com.project.backend.services.inter;

import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;

import java.util.List;

public interface AnswerService {
    List<Answer> create (List<String> answerRequest, Voting voting);
    List<Answer> update(List<String> newText, Voting voting);
    long vote(long id);
    List<Answer> findAllByVoting(long votingId);
    Answer findById(long id);
}

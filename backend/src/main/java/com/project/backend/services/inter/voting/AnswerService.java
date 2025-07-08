package com.project.backend.services.inter.voting;

import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;

import java.util.List;

public interface AnswerService {
    List<Answer> create (List<String> answerRequest, Voting voting);
    List<Answer> update(List<String> newText, Voting voting);
    void vote(long id);
    List<Answer> findAllByVoting(long votingId);
    Answer findById(long id);

    void decrement(long answerId);
}

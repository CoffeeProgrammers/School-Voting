package com.project.backend.services.impl;

import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.voting.AnswerRepository;
import com.project.backend.services.inter.AnswerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    @Override
    public List<Answer> create(List<String> answerRequest, Voting voting) {
        log.info("Service: Creating answer request {}", answerRequest);
        List<Answer> answers = new ArrayList<>();
        for(String answer : answerRequest){
            answers.add(answerRepository.save(new Answer(answer, voting)));
        }
        return answers;
    }

    @Override
    public List<Answer> update(List<String> newText, Voting voting) {
        log.info("Service: Updating answer request {}", newText);
        List<Answer> answers = findAllByVoting(voting.getId());
        Answer answer = null;
        for(int i = 0; i < newText.size(); i++){
            answer = answers.get(i);
            answer.setName(newText.get(i));
            answers.add(answerRepository.save(answer));
        }
        return answers;
    }

    @Override
    public void vote(long id) {
        log.info("Service: Vote request {}", id);
        Answer answer = findById(id);
        answer.incrementCount();
        answerRepository.save(answer);
    }

    @Override
    public List<Answer> findAllByVoting(long votingId) {
        log.info("Service: Finding all answers for voting {}", votingId);
        return answerRepository.findAllByVoting_Id(votingId);
    }

    @Override
    public Answer findById(long id) {
        log.info("Service: Finding answer by id {}", id);
        return answerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Override
    public void decrement(long answerId){
        log.info("Service: Decrement answer by id {}", answerId);
        Answer answer = findById(answerId);
        answer.setCount(answer.getCount() - 1);
        answerRepository.save(answer);
    }
}

package com.project.backend.services.impl.voting;

import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.repos.voting.AnswerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AnswerServiceImplTest {

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Mock
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAllAnswers() {
        Voting voting = new Voting();
        List<String> input = List.of("Yes", "No");

        when(answerRepository.save(any(Answer.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        List<Answer> result = answerService.create(input, voting);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Yes");
        assertThat(result.get(1).getName()).isEqualTo("No");
        verify(answerRepository, times(2)).save(any(Answer.class));
    }

    @Test
    void update_shouldUpdateAllAnswers() {
        Voting voting = new Voting();
        voting.setId(1L);

        List<Answer> existingAnswers = new ArrayList<>(List.of(
                new Answer("Old1", voting),
                new Answer("Old2", voting)
        ));
        existingAnswers.get(0).setId(101L);
        existingAnswers.get(1).setId(102L);

        List<String> updatedText = List.of("New1", "New2");

        when(answerRepository.findAllByVoting_Id(1L)).thenReturn(existingAnswers);
        when(answerRepository.save(any(Answer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Answer> result = answerService.update(updatedText, voting);

        assertThat(result).hasSize(4);
        assertThat(result.get(2).getName()).isEqualTo("New1");
        assertThat(result.get(3).getName()).isEqualTo("New2");

        verify(answerRepository, times(2)).save(any(Answer.class));
    }

    @Test
    void vote_shouldIncrementAnswerCount() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setCount(5);

        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        answerService.vote(1L);

        assertThat(answer.getCount()).isEqualTo(6);
        verify(answerRepository).save(answer);
    }

    @Test
    void decrement_shouldDecreaseAnswerCount() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setCount(3);

        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        answerService.decrement(1L);

        assertThat(answer.getCount()).isEqualTo(2);
        verify(answerRepository).save(answer);
    }

    @Test
    void findAllByVoting_shouldReturnAnswers() {
        List<Answer> answers = List.of(new Answer(), new Answer());
        when(answerRepository.findAllByVoting_Id(2L)).thenReturn(answers);

        List<Answer> result = answerService.findAllByVoting(2L);

        assertThat(result).hasSize(2);
        verify(answerRepository).findAllByVoting_Id(2L);
    }

    @Test
    void findById_shouldReturnAnswer_whenFound() {
        Answer answer = new Answer();
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        Answer result = answerService.findById(1L);

        assertThat(result).isEqualTo(answer);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(answerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> answerService.findById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Answer not found");
    }
}

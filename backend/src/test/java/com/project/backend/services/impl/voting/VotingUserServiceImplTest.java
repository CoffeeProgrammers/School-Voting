package com.project.backend.services.impl.voting;

import com.project.backend.models.User;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.models.voting.VotingUser;
import com.project.backend.repositories.repos.voting.VotingUserRepository;
import com.project.backend.services.inter.voting.AnswerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VotingUserServiceImplTest {

    @Mock
    private VotingUserRepository votingUserRepository;

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private VotingUserServiceImpl votingUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWithVotingAndUsers_shouldSaveAll() {
        Voting voting = mock(Voting.class);
        when(voting.getId()).thenReturn(1L);
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        List<User> users = Arrays.asList(user1, user2);

        List<VotingUser> saved = Arrays.asList(
                new VotingUser(voting, user1),
                new VotingUser(voting, user2)
        );

        when(votingUserRepository.saveAll(anyList())).thenReturn(saved);

        List<VotingUser> result = votingUserService.create(voting, users);

        assertEquals(2, result.size());
        verify(votingUserRepository).saveAll(anyList());
    }

    @Test
    void createWithVotingListAndUser_shouldSaveAll() {
        User user = mock(User.class);
        Voting v1 = mock(Voting.class);
        Voting v2 = mock(Voting.class);
        List<Voting> votings = Arrays.asList(v1, v2);

        List<VotingUser> saved = Arrays.asList(
                new VotingUser(v1, user),
                new VotingUser(v2, user)
        );

        when(votingUserRepository.saveAll(anyList())).thenReturn(saved);

        List<VotingUser> result = votingUserService.create(votings, user);

        assertEquals(2, result.size());
        verify(votingUserRepository).saveAll(anyList());
    }

    @Test
    void update_existingVotingUser_shouldSaveUpdatedAnswer() {
        Voting voting = mock(Voting.class);
        User user = mock(User.class);
        Answer answer = mock(Answer.class);

        when(voting.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(2L);
        when(answer.getId()).thenReturn(3L);

        VotingUser existingVotingUser = new VotingUser(voting, user);
        when(votingUserRepository.findById(any())).thenReturn(Optional.of(existingVotingUser));
        when(votingUserRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        VotingUser result = votingUserService.update(voting, user, answer);

        assertEquals(answer, result.getAnswer());
        verify(votingUserRepository).save(existingVotingUser);
    }

    @Test
    void update_notFoundVotingUser_shouldThrow() {
        Voting voting = mock(Voting.class);
        User user = mock(User.class);
        Answer answer = mock(Answer.class);

        when(voting.getId()).thenReturn(1L);
        when(user.getId()).thenReturn(2L);

        when(votingUserRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            votingUserService.update(voting, user, answer);
        });
    }

    @Test
    void findById_found_shouldReturnVotingUser() {
        VotingUser.VotingUserId id = new VotingUser.VotingUserId(2L, 1L);
        VotingUser votingUser = new VotingUser();

        when(votingUserRepository.findById(id)).thenReturn(Optional.of(votingUser));

        VotingUser result = votingUserService.findById(1L, 2L);

        assertEquals(votingUser, result);
    }

    @Test
    void findById_notFound_shouldThrow() {
        VotingUser.VotingUserId id = new VotingUser.VotingUserId(2L, 1L);

        when(votingUserRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> votingUserService.findById(1L, 2L));
    }

    @Test
    void existsById_trueAndFalse() {
        VotingUser.VotingUserId id = new VotingUser.VotingUserId(2L, 1L);

        when(votingUserRepository.existsById(id)).thenReturn(true).thenReturn(false);

        assertTrue(votingUserService.existsById(1L, 2L));
        assertFalse(votingUserService.existsById(1L, 2L));
    }

    @Test
    void countAllByVoting_returnsCount() {
        when(votingUserRepository.countAllByVoting_Id(1L)).thenReturn(5L);

        Long count = votingUserService.countAllByVoting(1L);

        assertEquals(5L, count);
    }

    @Test
    void countAllByVotingAnswered_returnsCount() {
        when(votingUserRepository.countAllByVoting_IdAndAnswerNotNull(1L)).thenReturn(3L);

        Long count = votingUserService.countAllByVotingAnswered(1L);

        assertEquals(3L, count);
    }

    @Test
    void deleteWithUser_noVotingUsers_shouldNotCallDeleteOrDecrement() {
        long userId = 10L;
        when(votingUserRepository.findAllByUser_Id(userId)).thenReturn(Collections.emptyList());

        votingUserService.deleteWithUser(userId);

        verify(votingUserRepository, never()).deleteAll(anyList());
        verify(answerService, never()).decrement(anyLong());
    }

    @Test
    void deleteWithUser_withVotingUsers_callsDecrementAndDeleteAll() {
        long userId = 10L;
        Voting votingActive = mock(Voting.class);
        Voting votingInactive = mock(Voting.class);
        Answer answer1 = mock(Answer.class);
        Answer answer2 = null;

        when(votingActive.now()).thenReturn(true);
        when(votingInactive.now()).thenReturn(false);

        VotingUser votingUser1 = new VotingUser();
        votingUser1.setVoting(votingActive);
        votingUser1.setAnswer(answer1);

        VotingUser votingUser2 = new VotingUser();
        votingUser2.setVoting(votingActive);
        votingUser2.setAnswer(answer2);

        VotingUser votingUser3 = new VotingUser();
        votingUser3.setVoting(votingInactive);
        votingUser3.setAnswer(answer1);

        List<VotingUser> votingUsers = Arrays.asList(votingUser1, votingUser2, votingUser3);

        when(votingUserRepository.findAllByUser_Id(userId)).thenReturn(votingUsers);

        votingUserService.deleteWithUser(userId);

        // Викликаємо decrement тільки для answer1, бо answer2 == null, а votingInactive.now() == false
        verify(answerService, times(1)).decrement(answer1.getId());
        verify(votingUserRepository).deleteAll(votingUsers);
    }
}

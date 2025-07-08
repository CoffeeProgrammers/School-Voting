package com.project.backend.services.impl.voting;

import com.project.backend.TestUtil;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.repos.voting.VotingRepository;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.voting.AnswerService;
import com.project.backend.services.inter.voting.VotingUserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VotingServiceImplTest {

    @Mock
    private VotingRepository votingRepository;
    @Mock
    private AnswerService answerService;
    @Mock
    private UserService userService;
    @Mock
    private VotingUserService votingUserService;

    @InjectMocks
    private VotingServiceImpl votingService;

    private Voting voting;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setSchool(TestUtil.createSchool("Name"));
        user.setMyClass(TestUtil.createClass("Name"));
        voting = new Voting();
        voting.setId(1L);
        voting.setName("Test Voting");
        voting.setDescription("Description");
        voting.setLevelType(LevelType.SCHOOL);
        voting.setCreator(user);
        voting.setStartTime(LocalDateTime.now().plusDays(1));
        voting.setEndTime(LocalDateTime.now().plusDays(2));
    }

    @Test
    void create_schoolLevel_shouldThrow_whenCreatorSchoolMismatch() {
        Voting voting = new Voting();
        voting.setLevelType(LevelType.SCHOOL);
        List<String> answers = List.of("ans");
        List<Long> targetIds = List.of(10L);
        long schoolId = 5L; // Інша школа
        long userId = 1L;

        User creator = mock(User.class);
        School creatorSchool = mock(School.class);
        when(userService.findById(userId)).thenReturn(creator);
        when(creator.getSchool()).thenReturn(creatorSchool);

        when(votingRepository.save(voting)).thenReturn(voting);

        when(creatorSchool.getId()).thenReturn(6L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                votingService.create(voting, answers, targetIds, schoolId, userId));

        assertTrue(ex.getMessage().contains("Can`t create voting and not be in that school"));
    }

    @Test
    void create_classLevel_success_forTeacherOrDirector() {
        Voting voting = new Voting();
        voting.setLevelType(LevelType.CLASS);
        List<String> answers = List.of("ans");
        List<Long> targetIds = List.of(11L);
        long userId = 1L;
        School school = TestUtil.createSchool("s");

        User creator = mock(User.class);
        when(userService.findById(userId)).thenReturn(creator);
        when(creator.getRole()).thenReturn("TEACHER"); // директор або викладач

        // userService.findAllByClass
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        when(userService.findAllByClass(11L, userId)).thenReturn(List.of(user1, user2));
        when(user1.getSchool()).thenReturn(school);
        when(user2.getSchool()).thenReturn(school);

        when(votingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Voting result = votingService.create(voting, answers, targetIds, school.getId(), userId);

        verify(userService).findAllByClass(11L, userId);
        verify(votingUserService).create(any(Voting.class), anyList());
        assertEquals(2, result.getCountAll());
    }

    @Test
    void update_shouldUpdateVoting() {
        when(votingRepository.findById(1L)).thenReturn(Optional.of(voting));
        when(votingRepository.save(any())).thenReturn(voting);

        Voting updated = votingService.update(voting, List.of("Updated"), 1L);

        assertEquals("Test Voting", updated.getName());
        verify(answerService).update(anyList(), any());
    }

    @Test
    void delete_shouldRemoveVoting() {
        voting.setStartTime(LocalDateTime.now().plusDays(1));
        when(votingRepository.findById(1L)).thenReturn(Optional.of(voting));

        votingService.delete(1L);

        verify(votingRepository).deleteById(1L);
    }

    @Test
    void findById_shouldReturnVoting() {
        when(votingRepository.findById(1L)).thenReturn(Optional.of(voting));

        Voting found = votingService.findById(1L);

        assertEquals(1L, found.getId());
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        when(votingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> votingService.findById(1L));
    }

    @Test
    void vote_shouldCallVoteServices() {
        Answer answer = new Answer("Option", voting);
        answer.setId(1L);

        voting.setStartTime(LocalDateTime.now().minusDays(1));
        voting.setEndTime(LocalDateTime.now().plusDays(1));

        when(votingRepository.findById(1L)).thenReturn(Optional.of(voting));
        when(answerService.findById(1L)).thenReturn(answer);

        votingService.vote(1L, 1L, user);

        verify(answerService).vote(1L);
        verify(votingUserService).update(voting, user, answer);
    }

    @Test
    void deletingUser_shouldUpdateCreatorToDeleted() {
        Voting voting2 = new Voting();
        voting2.setCreator(user);
        when(votingRepository.findAll(any(Specification.class))).thenReturn(List.of(voting2));
        when(userService.findUserByEmail(anyString())).thenReturn(user);

        votingService.deletingUser(1L);

        verify(votingRepository).saveAll(anyList());
    }

    @Test
    void findAllByUser_shouldReturnList() {
        when(votingRepository.findAll(any(Specification.class))).thenReturn(List.of(voting));

        List<Voting> result = votingService.findAllByUser(1L);

        assertFalse(result.isEmpty());
    }

    @Test
    void findAllByUserPaged_shouldReturnPage() {
        Page<Voting> page = new PageImpl<>(List.of(voting));
        when(votingRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        Page<Voting> result = votingService.findAllByUser(1L, null, null, null, 0, 10);

        assertEquals(1, result.getTotalElements());
    }

    @Nested
    class FindAllByUserTests {

        @Test
        void whenAllParamsNull_thenUseOnlyByUserSpecification() {
            long userId = 1L;
            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllByUser(userId, null, null, null, 0, 10);

            assertNotNull(page);
            verify(votingRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        void whenNameGivenAndNowTrueAndIsNotVotedTrue_thenBuildFullSpec() {
            long userId = 1L;
            String name = "VoteName";
            Boolean now = true;
            Boolean isNotVoted = true;

            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllByUser(userId, name, now, isNotVoted, 1, 5);

            assertNotNull(page);
            verify(votingRepository).findAll(any(Specification.class), eq(PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "endTime"))));
        }

        @Test
        void whenNowFalseAndIsNotVotedFalse_thenBuildSpecWithEndedAndIsVoted() {
            long userId = 2L;
            Boolean now = false;
            Boolean isNotVoted = false;

            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllByUser(userId, null, now, isNotVoted, 0, 10);

            assertNotNull(page);
            verify(votingRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        void whenNowNullAndMyFalseAndNotStartedTrue_thenBuildSpecWithByStartTimeAndByStartTimeNot() {
            long userId = 3L;
            Boolean now = null;
            Boolean isNotVoted = null;

            // createSpecification параметр my = false для findAllByUser
            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllByUser(userId, null, now, isNotVoted, 0, 10);

            assertNotNull(page);
            verify(votingRepository).findAll(any(Specification.class), any(Pageable.class));
        }
    }

    // --- findAllByCreator ---

    @Nested
    class FindAllByCreatorTests {

        @Test
        void whenAllParamsNull_thenUseByCreatorOnly() {
            long userId = 10L;

            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllByCreator(userId, null, null, null, 0, 10);

            assertNotNull(page);
            verify(votingRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        void whenNameNotNullAndNowTrueAndNotStartedTrue_thenCreateSpecWithAll() {
            long userId = 11L;
            String name = "CreatorVote";
            Boolean now = true;
            Boolean notStarted = true;

            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllByCreator(userId, name, now, notStarted, 1, 20);

            assertNotNull(page);
            verify(votingRepository).findAll(any(Specification.class), eq(PageRequest.of(1, 20, Sort.by(Sort.Direction.DESC, "endTime"))));
        }

        @Test
        void whenNowFalseAndNotStartedFalse_thenCreateSpecWithoutNotStarted() {
            long userId = 12L;
            Boolean now = false;
            Boolean notStarted = false;

            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllByCreator(userId, null, now, notStarted, 0, 10);

            assertNotNull(page);
            verify(votingRepository).findAll((Specification<Voting>) any(), (Pageable) any());
        }
    }

    // --- findAllForDirector ---

    @Nested
    class FindAllForDirectorTests {

        @Test
        void whenAllParamsNull_thenUseByDirectorOnly() {
            long userId = 100L;

            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllForDirector(0L, userId, null, null, 0, 10);

            assertNotNull(page);
            verify(votingRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        void whenNameGivenNowTrue_thenBuildSpecWithByNameAndByStartAndEndTime() {
            long userId = 101L;
            String name = "DirectorVote";
            Boolean now = true;

            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            Page<Voting> page = votingService.findAllForDirector(0L, userId, name, now, 2, 7);

            assertNotNull(page);
            verify(votingRepository).findAll(any(Specification.class), eq(PageRequest.of(2, 7, Sort.by(Sort.Direction.DESC, "endTime"))));
        }
    }

    // --- createSpecification (непрямо через findAllByUser) ---

    @Nested
    class CreateSpecificationIndirectTests {

        @Test
        void withAllParamsVariousValues_shouldReturnNonNullSpecification() {
            long userId = 1L;
            when(votingRepository.findAll((Specification<Voting>) any(), (Pageable) any())).thenReturn(Page.empty());

            // варіанти для тесту createSpecification:
            votingService.findAllByUser(userId, "Name", true, true, 0, 10);
            votingService.findAllByUser(userId, "Name", false, false, 0, 10);
            votingService.findAllByUser(userId, null, null, null, 0, 10);
            votingService.findAllByUser(userId, null, false, true, 0, 10);
            votingService.findAllByUser(userId, "Name", null, null, 0, 10);

            verify(votingRepository, times(5)).findAll(any(Specification.class), any(Pageable.class));
        }
    }

    @Test
    void findAllByClass_shouldReturnList() {
        when(votingRepository.findAll(any(Specification.class))).thenReturn(List.of(voting));

        List<Voting> result = votingService.findAllByClass(1L);

        assertEquals(1, result.size());
    }

    @Test
    void findAllByUserAndLevelClass_shouldReturnList() {
        long userId = 1L;
        List<Voting> expectedVotings = List.of(TestUtil.createVoting("S"), TestUtil.createVoting("E"));

        when(votingRepository.findAll((Specification<Voting>)any())).thenReturn(expectedVotings);

        List<Voting> result = votingService.findAllByUserAndLevelClass(userId);

        assertEquals(expectedVotings, result);
        verify(votingRepository).findAll((Specification<Voting>) any());
    }

    @Test
    void deleteBy_shouldCallDeleteAllByCreatorSchoolId_whenLevelTypeSchool() {
        LevelType levelType = LevelType.SCHOOL;
        long targetId = 5L;

        doNothing().when(votingRepository).deleteAllByCreator_School_Id(targetId);

        votingService.deleteBy(levelType, targetId);

        verify(votingRepository).deleteAllByCreator_School_Id(targetId);
        verify(votingRepository, never()).deleteAllByCreator_MyClass_Id(anyLong());
    }

    @Test
    void deleteBy_shouldCallDeleteAllByCreatorMyClassId_whenLevelTypeNotSchool() {
        LevelType levelType = LevelType.CLASS; // або інший, відмінний від SCHOOL
        long targetId = 7L;

        doNothing().when(votingRepository).deleteAllByCreator_MyClass_Id(targetId);

        votingService.deleteBy(levelType, targetId);

        verify(votingRepository).deleteAllByCreator_MyClass_Id(targetId);
        verify(votingRepository, never()).deleteAllByCreator_School_Id(anyLong());
    }
}

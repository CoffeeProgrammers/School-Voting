package com.project.backend.services.impl.petition;

import com.project.backend.TestUtil;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
import com.project.backend.models.petition.Petition;
import com.project.backend.repositories.repos.petition.PetitionRepository;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.petition.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetitionServiceImplTest {

    @Mock private PetitionRepository petitionRepository;
    @Mock private UserService userService;
    @Mock private CommentService commentService;

    @InjectMocks private PetitionServiceImpl petitionService;

    private User creator;
    private Petition petition;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        creator = new User();
        creator.setId(1L);
        creator.setSchool(TestUtil.createSchool("User"));
        creator.setMyClass(TestUtil.createClass("User"));

        petition = new Petition();
        petition.setId(10L);
        petition.setLevelType(LevelType.SCHOOL);
        petition.setStatus(Status.ACTIVE);
        petition.setTargetId(100L);
        petition.setUsers(new HashSet<>(Set.of(TestUtil.createUser("New", "new@some"))));
        petition.setCount(0);
        petition.setCountNeeded(5);
        petition.setCreator(creator);
    }

    @Test
    void create_shouldThrowForInvalidLevelType() {
        petition.setLevelType(LevelType.GROUP_OF_PARENTS_AND_STUDENTS);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            petitionService.create(petition, 100L, creator, "target");
        });

        assertEquals("Cannot create a petition with such level type.", ex.getMessage());
    }

    @Test
    void create_shouldThrowIfCreatorNotInClassOrSchool() {
        petition.setLevelType(LevelType.CLASS);
        creator.setMyClass(new com.project.backend.models.Class());
        creator.getMyClass().setId(1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            petitionService.create(petition, 2L, creator, "target");
        });
        assertEquals("Cannot create a petition in class where u are not.", ex.getMessage());

        petition.setLevelType(LevelType.SCHOOL);
        creator.setSchool(new com.project.backend.models.School());
        creator.getSchool().setId(1L);

        ex = assertThrows(IllegalArgumentException.class, () -> {
            petitionService.create(petition, 2L, creator, "target");
        });
        assertEquals("Cannot create a petition in school where u are not.", ex.getMessage());
    }

    @Test
    void create_shouldSetDefaultsAndSave() {
        petition.setLevelType(LevelType.SCHOOL);
        creator.setSchool(new com.project.backend.models.School());
        creator.getSchool().setId(100L);

        when(petitionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Petition result = petitionService.create(petition, 100L, creator, "targetName");

        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(creator, result.getCreator());
        assertEquals(100L, result.getTargetId());
        assertEquals("targetName", result.getTargetName());
        assertNotNull(result.getCreationTime());
        assertEquals(result.getCreationTime().plusDays(45), result.getEndTime());
    }

    @Test
    void delete_shouldCallCommentServiceAndRepository() {
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        petitionService.delete(10L);

        verify(commentService).deleteWithPetition(10L);
        verify(petitionRepository).deleteById(10L);
    }

    @Test
    void delete_shouldThrowIfNotFound() {
        when(petitionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> petitionService.delete(10L));
    }

    @Test
    void support_shouldThrowIfNotActive() {
        petition.setStatus(Status.REJECTED);
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            petitionService.support(10L, creator);
        });
        assertEquals("Petition is not active.", ex.getMessage());
    }

    @Test
    void support_shouldThrowIfEnded() {
        petition.setStatus(Status.ACTIVE);
        petition.setEndTime(LocalDateTime.now().minusDays(1));
        petition.setCreationTime(LocalDateTime.now().minusDays(46));
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            petitionService.support(10L, creator);
        });
        assertEquals("Petition is ended.", ex.getMessage());
    }

    @Test
    void support_shouldThrowIfUserAlreadySupported() {
        petition.setStatus(Status.ACTIVE);
        petition.getUsers().add(creator);
        petition.setEndTime(LocalDateTime.now().plusDays(2));
        petition.setCreationTime(LocalDateTime.now().minusDays(46));
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            petitionService.support(10L, creator);
        });
        assertEquals("Cannot support petition because user is already petition", ex.getMessage());
    }

    @Test
    void support_shouldIncrementCountAndSave() {
        petition.setStatus(Status.ACTIVE);
        petition.setEndTime(LocalDateTime.now().plusDays(2));
        petition.setCreationTime(LocalDateTime.now().minusDays(46));
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));
        when(petitionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.countAllBySchoolAndRole(anyLong(), anyString())).thenReturn(10L);

        petition.getUsers().clear();

        long count = petitionService.support(10L, creator);

        assertEquals(1, count);
        assertEquals(Status.ACTIVE, petition.getStatus()); // depends on checkingStatus logic
    }

    @Test
    void approve_shouldSetStatusApprovedIfWaiting() {
        petition.setStatus(Status.WAITING_FOR_CONSIDERATION);
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        petitionService.approve(10L);

        assertEquals(Status.APPROVED, petition.getStatus());
        verify(petitionRepository).save(petition);
    }

    @Test
    void approve_shouldThrowIfNotWaiting() {
        petition.setStatus(Status.ACTIVE);
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            petitionService.approve(10L);
        });

        assertEquals("Petition is not waiting.", ex.getMessage());
    }

    @Test
    void reject_shouldSetStatusRejectedIfWaiting() {
        petition.setStatus(Status.WAITING_FOR_CONSIDERATION);
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        petitionService.reject(10L);

        assertEquals(Status.REJECTED, petition.getStatus());
        verify(petitionRepository).save(petition);
    }

    @Test
    void reject_shouldThrowIfNotWaiting() {
        petition.setStatus(Status.ACTIVE);
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            petitionService.reject(10L);
        });

        assertEquals("Petition is not waiting.", ex.getMessage());
    }

    @Test
    void findById_shouldReturnPetition() {
        when(petitionRepository.findById(10L)).thenReturn(Optional.of(petition));

        Petition result = petitionService.findById(10L);

        assertEquals(petition, result);
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        when(petitionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> petitionService.findById(10L));
    }

    @Test
    void findAllMy_shouldCallRepository() {
        when(userService.findById(1L)).thenReturn(creator);
        when(petitionRepository.findAll((Specification<Petition>)any())).thenReturn(List.of(petition));

        List<Petition> result = petitionService.findAllMy(1L);

        assertFalse(result.isEmpty());
        verify(petitionRepository).findAll((Specification<Petition>)any());
    }

    @Test
    void findAllMy_withFilters_shouldCallRepository() {
        when(userService.findById(1L)).thenReturn(creator);
        when(petitionRepository.findAll((Specification<Petition>) any(), any(PageRequest.class))).thenReturn(Page.empty());

        petitionService.findAllMy("name", "ACTIVE", 0, 10, 1L);

        verify(petitionRepository).findAll((Specification<Petition>) any(), any(PageRequest.class));
    }

    @Test
    void findAllByCreator_shouldCallRepository() {
        when(petitionRepository.findAll((Specification<Petition>)any(), any(PageRequest.class))).thenReturn(Page.empty());

        petitionService.findAllByCreator("name", "ACTIVE", 0, 10, 1L);

        verify(petitionRepository).findAll((Specification<Petition>)any(), any(PageRequest.class));
    }

    @Test
    void findAllForDirector_shouldCallRepository() {
        when(petitionRepository.findAll((Specification<Petition>)any(), any(PageRequest.class))).thenReturn(Page.empty());

        petitionService.findAllForDirector("name", "ACTIVE", 100L, 0, 10);

        verify(petitionRepository).findAll((Specification<Petition>)any(), any(PageRequest.class));
    }

    @Test
    void deletingUser_shouldReplaceCreatorAndDeleteVotes() {
        User deletedUser = TestUtil.createUser("user", "!deleted-user!@deleted.com");

        petition.setEndTime(LocalDateTime.now().plusDays(2));
        petition.setCreationTime(LocalDateTime.now().minusDays(46));

        when(petitionRepository.findAll((Specification<Petition>)any())).thenReturn(List.of(petition));
        when(userService.findUserByEmail("!deleted-user!@deleted.com")).thenReturn(deletedUser);
        when(userService.findById(creator.getId())).thenReturn(creator);

        petitionService.deletingUser(creator.getId());

        verify(petitionRepository, times(2)).saveAll(any());
    }

    @Test
    void deleteVoteByUser_shouldDecrementCountAndCheckStatus() {
        when(petitionRepository.findAll((Specification<Petition>)any())).thenReturn(List.of(petition));
        when(userService.findById(creator.getId())).thenReturn(creator);

        petition.setEndTime(LocalDateTime.now().plusDays(2));
        petition.setCreationTime(LocalDateTime.now().minusDays(46));
        petition.setStatus(Status.ACTIVE);
        petition.setCount(5);

        petitionService.deleteVoteByUser(creator.getId());

        verify(petitionRepository).saveAll(any());
    }

    @Test
    void findAllByUserAndLevelClass_shouldCallRepository() {
        when(userService.findById(1L)).thenReturn(creator);
        when(petitionRepository.findAll((Specification<Petition>)any())).thenReturn(List.of(petition));

        List<Petition> result = petitionService.findAllByUserAndLevelClass(1L);

        assertFalse(result.isEmpty());
        verify(petitionRepository).findAll((Specification<Petition>)any());
    }

    @Test
    void findAllByClass_shouldCallRepository() {
        when(petitionRepository.findAll((Specification<Petition>)any())).thenReturn(List.of(petition));

        List<Petition> result = petitionService.findAllByClass(100L);

        assertFalse(result.isEmpty());
        verify(petitionRepository).findAll((Specification<Petition>)any());
    }

    @Test
    void deleteBy_shouldDeletePetitionsAndComments_BySchool() {
        // given
        long schoolId = 1L;
        Petition petition1 = new Petition(); petition1.setId(101L);
        Petition petition2 = new Petition(); petition2.setId(102L);
        List<Petition> petitions = List.of(petition1, petition2);

        when(petitionRepository.findAll((Specification<Petition>) any())).thenReturn(petitions);

        // when
        petitionService.deleteBy(LevelType.SCHOOL, schoolId);

        // then
        verify(commentService).deleteWithPetition(101L);
        verify(commentService).deleteWithPetition(102L);
        verify(petitionRepository).deleteAllByLevelTypeAndTargetId(LevelType.SCHOOL, schoolId);
    }

    @Test
    void deleteBy_shouldDeletePetitionsAndComments_ByClass() {
        // given
        long classId = 2L;
        Petition petition = new Petition(); petition.setId(201L);
        when(petitionRepository.findAll((Specification<Petition>) any())).thenReturn(List.of(petition));

        // when
        petitionService.deleteBy(LevelType.CLASS, classId);

        // then
        verify(commentService).deleteWithPetition(201L);
        verify(petitionRepository).deleteAllByLevelTypeAndTargetId(LevelType.CLASS, classId);
    }
}

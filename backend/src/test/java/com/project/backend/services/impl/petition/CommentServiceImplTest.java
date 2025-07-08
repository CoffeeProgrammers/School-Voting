package com.project.backend.services.impl.petition;

import com.project.backend.models.User;
import com.project.backend.models.petition.Comment;
import com.project.backend.models.petition.Petition;
import com.project.backend.repositories.repos.petition.CommentRepository;
import com.project.backend.services.inter.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment;
    private User user;
    private Petition petition;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        petition = new Petition();
        petition.setId(10L);

        comment = new Comment();
        comment.setId(100L);
        comment.setText("Test comment");
        comment.setCreator(user);
        comment.setPetition(petition);
    }

    @Test
    void create_shouldSaveComment() {
        when(commentRepository.save(any(Comment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = commentService.create(new Comment(), user, petition);

        assertNotNull(result);
        assertEquals(petition, result.getPetition());
        assertEquals(user, result.getCreator());
        assertNotNull(result.getCreatedTime());
        verify(commentRepository).save(any());
    }

    @Test
    void update_shouldModifyText() {
        Comment updated = new Comment();
        updated.setText("Updated text");

        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = commentService.update(updated, 100L);

        assertEquals("Updated text", result.getText());
        verify(commentRepository).save(comment);
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                commentService.update(new Comment(), 999L));
    }

    @Test
    void delete_shouldCallRepository() {
        commentService.delete(100L);
        verify(commentRepository).deleteById(100L);
    }

    @Test
    void deleteWithPetition_shouldCallRepository() {
        commentService.deleteWithPetition(10L);
        verify(commentRepository).deleteAllByPetition_Id(10L);
    }

    @Test
    void findAllByPetition_shouldReturnPage() {
        Page<Comment> page = new PageImpl<>(List.of(comment));
        when(commentRepository.findAllByPetition_Id(eq(10L), any())).thenReturn(page);

        Page<Comment> result = commentService.findAllByPetition(10L, 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(comment, result.getContent().get(0));
        verify(commentRepository).findAllByPetition_Id(eq(10L), any(Pageable.class));
    }

    @Test
    void findById_shouldReturnComment() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(comment));

        Comment result = commentService.findById(100L);

        assertEquals(comment, result);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                commentService.findById(999L));
    }

    @Test
    void deleteingUser_shouldReplaceCreatorWithDeletedUser() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setCreator(user);

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setCreator(user);

        List<Comment> userComments = List.of(comment1, comment2);

        User deletedUser = new User();
        deletedUser.setEmail("!deleted-user!@deleted.com");

        when(commentRepository.findAllByCreator_Id(1L)).thenReturn(userComments);
        when(userService.findUserByEmail("!deleted-user!@deleted.com")).thenReturn(deletedUser);

        commentService.deleteingUser(1L);

        assertEquals(deletedUser, comment1.getCreator());
        assertEquals(deletedUser, comment2.getCreator());
        verify(commentRepository).saveAll(userComments);
    }
}

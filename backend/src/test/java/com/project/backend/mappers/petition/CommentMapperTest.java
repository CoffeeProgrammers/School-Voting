package com.project.backend.mappers.petition;

import com.project.backend.dto.comment.CommentRequest;
import com.project.backend.dto.comment.CommentResponse;
import com.project.backend.models.User;
import com.project.backend.models.petition.Comment;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentMapperTest {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void fromRequestToComment_null() {
        assertNull(commentMapper.fromRequestToComment(null));
    }

    @Test
    void fromRequestToComment_success() {
        CommentRequest request = new CommentRequest();
        request.setText("Test comment");

        Comment comment = commentMapper.fromRequestToComment(request);

        assertNotNull(comment);
        assertEquals("Test comment", comment.getText());
    }

    @Test
    void fromCommentToResponse_null() {
        assertNull(commentMapper.fromCommentToResponse(null));
    }

    @Test
    void fromCommentToResponse_success() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Hello world");
        comment.setCreatedTime(LocalDateTime.of(2024, 1, 1, 10, 0));

        User creator = new User();
        creator.setId(7L);
        creator.setEmail("john@example.com");
        creator.setFirstName("John");
        creator.setLastName("Doe");

        comment.setCreator(creator);

        CommentResponse dto = commentMapper.fromCommentToResponse(comment);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Hello world", dto.getText());
        assertEquals("2024-01-01T10:00:00", dto.getCreatedTime());

        assertNotNull(dto.getCreator());
        assertEquals(7L, dto.getCreator().getId());
        assertEquals("john@example.com", dto.getCreator().getEmail());
        assertEquals("John", dto.getCreator().getFirstName());
        assertEquals("Doe", dto.getCreator().getLastName());
    }

    @Test
    void fromCommentToResponse_nullCreatorAndTime() {
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("No creator or time");

        CommentResponse dto = commentMapper.fromCommentToResponse(comment);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("No creator or time", dto.getText());
        assertNull(dto.getCreator());
        assertNull(dto.getCreatedTime());
    }
}

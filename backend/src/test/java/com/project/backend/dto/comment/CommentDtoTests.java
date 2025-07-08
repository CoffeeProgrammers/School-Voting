package com.project.backend.dto.comment;

import com.project.backend.dto.user.UserListResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CommentDtoTests {

    @Test
    void testCommentRequest() {
        CommentRequest request1 = new CommentRequest();
        request1.setText("Test comment");

        CommentRequest request2 = new CommentRequest();
        request2.setText("Test comment");

        assertNotEquals(request1, null);
        assertEquals(request1, request2);
        assertEquals(request1.toString(), request2.toString());
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testCommentResponse() {
        CommentResponse response1 = new CommentResponse();
        UserListResponse creator = new UserListResponse();
        response1.setId(1L);
        response1.setCreator(creator);
        response1.setText("Sample text");
        response1.setCreatedTime("2024-01-01T10:00:00");

        CommentResponse response2 = new CommentResponse();
        response2.setId(response1.getId());
        response2.setCreator(response1.getCreator());
        response2.setText(response1.getText());
        response2.setCreatedTime(response1.getCreatedTime());

        assertNotEquals(response1, null);
        assertEquals(response1, response2);
        assertEquals(response1.toString(), response2.toString());
        assertEquals(response1.hashCode(), response2.hashCode());
    }
}

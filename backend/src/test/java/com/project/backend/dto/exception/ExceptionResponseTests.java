package com.project.backend.dto.exception;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ExceptionResponseTests {

    @Test
    void testSingleMessageConstructor() {
        ExceptionResponse response1 = new ExceptionResponse("Error occurred");
        ExceptionResponse response2 = new ExceptionResponse(response1.getMessages().get(0));

        assertNotEquals(response1, null);
        assertEquals(List.of("Error occurred"), response1.getMessages());
        assertEquals(response1, response2);
        assertEquals(response1.toString(), response2.toString());
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testListMessageConstructor() {
        List<String> errors = List.of("Error 1", "Error 2");
        ExceptionResponse response1 = new ExceptionResponse(errors);
        ExceptionResponse response2 = new ExceptionResponse(response1.getMessages());

        assertNotEquals(response1, null);
        assertEquals(errors, response1.getMessages());
        assertEquals(response1, response2);
        assertEquals(response1.toString(), response2.toString());
        assertEquals(response1.hashCode(), response2.hashCode());
    }
}

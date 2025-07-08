package com.project.backend.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserUtilTest {

    @Test
    void extractErrorMessages_withErrorsArray() {
        String json = """
            {
              "errors": [
                {"errorMessage": "Error 1"},
                {"errorMessage": "Error 2"}
              ]
            }
            """;

        List<String> messages = JsonParserUtil.extractErrorMessages(json);

        assertNotNull(messages);
        assertEquals(2, messages.size());
        assertTrue(messages.contains("Error 1"));
        assertTrue(messages.contains("Error 2"));
    }

    @Test
    void extractErrorMessages_withSingleErrorMessage() {
        String json = """
            {
              "errorMessage": "Single error"
            }
            """;

        List<String> messages = JsonParserUtil.extractErrorMessages(json);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals("Single error", messages.get(0));
    }

    @Test
    void extractErrorMessages_withNoErrors() {
        String json = "{}";

        List<String> messages = JsonParserUtil.extractErrorMessages(json);

        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    void extractErrorMessages_withInvalidJson() {
        String json = "{ invalid json }";

        List<String> messages = JsonParserUtil.extractErrorMessages(json);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals("Unexpected error format", messages.get(0));
    }

    @Test
    void extractErrorMessages_withErrorsWithoutErrorMessageField() {
        String json = """
            {
              "errors": [
                {"message": "Not errorMessage field"},
                {"errorMessage": "Real error"}
              ]
            }
            """;

        List<String> messages = JsonParserUtil.extractErrorMessages(json);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals("Real error", messages.get(0));
    }

    @Test
    void testConstructor() {
        JsonParserUtil jsonParserUtil = new JsonParserUtil();
        assertEquals(JsonParserUtil.class, jsonParserUtil.getClass());
    }
}

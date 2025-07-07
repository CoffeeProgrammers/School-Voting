package com.project.backend.dto.answer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AnswerDtoTest {

    @Test
    void testAnswerResponse() {
        AnswerResponse dto1 = new AnswerResponse();
        dto1.setName("Name");
        dto1.setId(1L);
        dto1.setCount(10L);

        AnswerResponse dto2 = new AnswerResponse();
        dto2.setName(dto1.getName());
        dto2.setId(dto1.getId());
        dto2.setCount(dto1.getCount());

        assertNotEquals(null, dto1);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }
}

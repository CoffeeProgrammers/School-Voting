package com.project.backend.mappers.voting;

import com.project.backend.dto.answer.AnswerResponse;
import com.project.backend.models.voting.Answer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerMapperTest {

    private final AnswerMapper answerMapper = Mappers.getMapper(AnswerMapper.class);

    @Test
    void fromAnswerToResponse_null() {
        assertNull(answerMapper.fromAnswerToResponse(null));
    }

    @Test
    void fromAnswerToResponse_success() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setName("Yes");
        answer.setCount(42L);

        AnswerResponse dto = answerMapper.fromAnswerToResponse(answer);

        assertNotNull(dto);
        assertEquals(answer.getId(), dto.getId());
        assertEquals(answer.getName(), dto.getName());
        assertEquals(answer.getCount(), dto.getCount());
    }
}

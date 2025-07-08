package com.project.backend.dto.voting;

import com.project.backend.dto.answer.AnswerResponse;
import com.project.backend.dto.user.UserListResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class VotingDtoTest {

    @Test
    void testVotingFullResponse() {
        VotingFullResponse dto1 = new VotingFullResponse();
        dto1.setId(1L);
        dto1.setName("Name");
        dto1.setCreator(new UserListResponse());
        dto1.setDescription("Description");
        dto1.setEndTime(LocalDateTime.now().toString());
        dto1.setStartTime(LocalDateTime.now().toString());
        dto1.setMyAnswerId(1L);
        dto1.setStatistics(new VotingStatisticsResponse());

        VotingFullResponse dto2 = new VotingFullResponse();
        dto2.setId(dto1.getId());
        dto2.setName(dto1.getName());
        dto2.setCreator(dto1.getCreator());
        dto2.setDescription(dto1.getDescription());
        dto2.setEndTime(dto1.getEndTime());
        dto2.setStartTime(dto1.getStartTime());
        dto2.setMyAnswerId(dto1.getMyAnswerId());
        dto2.setStatistics(dto1.getStatistics());

        assertNotEquals(dto1, null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testVotingListResponse() {
        VotingListResponse dto1 = new VotingListResponse();
        dto1.setId(1L);
        dto1.setName("Name");
        dto1.setEndTime(LocalDateTime.now().toString());
        dto1.setStartTime(LocalDateTime.now().toString());
        dto1.setIsAnswered(false);

        VotingListResponse dto2 = new VotingListResponse();
        dto2.setId(dto1.getId());
        dto2.setName(dto1.getName());
        dto2.setEndTime(dto1.getEndTime());
        dto2.setStartTime(dto1.getStartTime());
        dto2.setIsAnswered(dto1.getIsAnswered());

        assertNotEquals(dto1, null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testVotingCreateRequest() {
        VotingCreateRequest dto1 = new VotingCreateRequest();
        dto1.setName("Name");
        dto1.setDescription("Description");
        dto1.setEndTime(LocalDateTime.now().toString());
        dto1.setStartTime(LocalDateTime.now().toString());
        dto1.setLevelType("LevelType");
        dto1.setAnswers(List.of("1", "2", "3"));

        VotingCreateRequest dto2 = new VotingCreateRequest();
        dto2.setName(dto1.getName());
        dto2.setDescription(dto1.getDescription());
        dto2.setEndTime(dto1.getEndTime());
        dto2.setStartTime(dto1.getStartTime());
        dto2.setLevelType(dto1.getLevelType());
        dto2.setAnswers(dto1.getAnswers());

        assertNotEquals(dto1, null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testVotingUpdateRequest() {
        VotingUpdateRequest dto1 = new VotingUpdateRequest();
        dto1.setName("Name");
        dto1.setDescription("Description");
        dto1.setAnswers(List.of("1", "2", "3"));

        VotingUpdateRequest dto2 = new VotingUpdateRequest();
        dto2.setName(dto1.getName());
        dto2.setDescription(dto1.getDescription());
        dto2.setAnswers(dto1.getAnswers());

        assertNotEquals(dto1, null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testVotingStatisticsResponse() {
        VotingStatisticsResponse dto1 = new VotingStatisticsResponse();
        dto1.setCountAll(10L);
        dto1.setCountAnswered(1L);
        dto1.setAnswers(List.of(new AnswerResponse()));

        VotingStatisticsResponse dto2 = new VotingStatisticsResponse();
        dto2.setCountAll(dto1.getCountAll());
        dto2.setCountAnswered(dto1.getCountAnswered());
        dto2.setAnswers(dto1.getAnswers());

        assertNotEquals(dto1, null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }
}

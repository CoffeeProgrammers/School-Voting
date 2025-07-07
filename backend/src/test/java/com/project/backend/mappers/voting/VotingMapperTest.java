package com.project.backend.mappers.voting;

import com.project.backend.dto.user.UserListResponse;
import com.project.backend.dto.voting.VotingCreateRequest;
import com.project.backend.dto.voting.VotingFullResponse;
import com.project.backend.dto.voting.VotingListResponse;
import com.project.backend.dto.voting.VotingUpdateRequest;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.voting.Voting;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class VotingMapperTest {

    private final VotingMapper votingMapper = Mappers.getMapper(VotingMapper.class);

    @Test
    void fromCreateRequestToVoting_null() {
        assertNull(votingMapper.fromRequestToVoting((VotingCreateRequest) null));
    }

    @Test
    void fromCreateRequestToVoting_success() {
        VotingCreateRequest req = new VotingCreateRequest();
        req.setName("Vote 1");
        req.setDescription("Description");
        req.setLevelType("SCHOOL");
        req.setStartTime("2025-07-07T15:00:00");
        req.setEndTime("2025-07-10T15:00:00");

        Voting voting = votingMapper.fromRequestToVoting(req);

        assertNotNull(voting);
        assertEquals("Vote 1", voting.getName());
        assertEquals("Description", voting.getDescription());
        assertEquals(LevelType.SCHOOL, voting.getLevelType());
        assertEquals(LocalDateTime.parse("2025-07-07T15:00:00"), voting.getStartTime());
        assertEquals(LocalDateTime.parse("2025-07-10T15:00:00"), voting.getEndTime());
    }

    @Test
    void fromUpdateRequestToVoting_null() {
        assertNull(votingMapper.fromRequestToVoting((VotingUpdateRequest) null));
    }

    @Test
    void fromUpdateRequestToVoting_success() {
        VotingUpdateRequest req = new VotingUpdateRequest();
        req.setName("Updated Vote");
        req.setDescription("Updated Desc");

        Voting voting = votingMapper.fromRequestToVoting(req);

        assertNotNull(voting);
        assertEquals("Updated Vote", voting.getName());
        assertEquals("Updated Desc", voting.getDescription());
    }

    @Test
    void fromVotingToFullResponse_null() {
        assertNull(votingMapper.fromVotingToFullResponse(null));
    }

    @Test
    void fromVotingToFullResponse_success() {
        Voting voting = new Voting();
        voting.setId(10L);
        voting.setName("Full Vote");
        voting.setDescription("Full Desc");
        voting.setLevelType(LevelType.SCHOOL);
        voting.setStartTime(LocalDateTime.of(2025, 7, 1, 10, 0));
        voting.setEndTime(LocalDateTime.of(2025, 7, 5, 10, 0));

        User creator = new User();
        creator.setId(3L);
        creator.setEmail("creator@example.com");
        creator.setFirstName("John");
        creator.setLastName("Doe");
        voting.setCreator(creator);

        VotingFullResponse dto = votingMapper.fromVotingToFullResponse(voting);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Full Vote", dto.getName());
        assertEquals("Full Desc", dto.getDescription());
        assertEquals("SCHOOL", dto.getLevelType());
        assertEquals("2025-07-01T10:00:00", dto.getStartTime());
        assertEquals("2025-07-05T10:00:00", dto.getEndTime());

        UserListResponse cr = dto.getCreator();
        assertNotNull(cr);
        assertEquals(3L, cr.getId());
        assertEquals("creator@example.com", cr.getEmail());
        assertEquals("John", cr.getFirstName());
        assertEquals("Doe", cr.getLastName());
    }

    @Test
    void fromVotingToFullResponse_nullOptionalFields() {
        Voting voting = new Voting();
        voting.setId(11L);
        voting.setName("No Optional");
        voting.setDescription("No Optional Fields");
        // levelType, startTime, endTime, creator = null

        VotingFullResponse dto = votingMapper.fromVotingToFullResponse(voting);

        assertNotNull(dto);
        assertEquals(11L, dto.getId());
        assertEquals("No Optional", dto.getName());
        assertEquals("No Optional Fields", dto.getDescription());

        assertNull(dto.getLevelType());
        assertNull(dto.getStartTime());
        assertNull(dto.getEndTime());
        assertNull(dto.getCreator());
    }

    @Test
    void fromVotingToListResponse_null() {
        assertNull(votingMapper.fromVotingToListResponse(null));
    }

    @Test
    void fromVotingToListResponse_success() {
        Voting voting = new Voting();
        voting.setId(20L);
        voting.setName("List Vote");
        voting.setLevelType(LevelType.SCHOOL);
        voting.setStartTime(LocalDateTime.of(2025, 8, 1, 9, 0));
        voting.setEndTime(LocalDateTime.of(2025, 8, 3, 9, 0));

        VotingListResponse dto = votingMapper.fromVotingToListResponse(voting);

        assertNotNull(dto);
        assertEquals(20L, dto.getId());
        assertEquals("List Vote", dto.getName());
        assertEquals("SCHOOL", dto.getLevelType());
        assertEquals("2025-08-01T09:00:00", dto.getStartTime());
        assertEquals("2025-08-03T09:00:00", dto.getEndTime());
    }

    @Test
    void fromVotingToListResponse_nullOptionalFields() {
        Voting voting = new Voting();
        voting.setId(21L);
        voting.setName("List Nulls");

        VotingListResponse dto = votingMapper.fromVotingToListResponse(voting);

        assertNotNull(dto);
        assertEquals(21L, dto.getId());
        assertEquals("List Nulls", dto.getName());

        assertNull(dto.getLevelType());
        assertNull(dto.getStartTime());
        assertNull(dto.getEndTime());
    }

    @Test
    void fromCreateRequestToVoting_nullOptionalFields() {
        VotingCreateRequest req = new VotingCreateRequest();
        req.setName("Vote Test");
        req.setDescription("Desc");

        req.setLevelType(null);
        req.setStartTime(null);
        req.setEndTime(null);

        Voting voting = votingMapper.fromRequestToVoting(req);

        assertNotNull(voting);
        assertEquals("Vote Test", voting.getName());
        assertEquals("Desc", voting.getDescription());

        assertNull(voting.getLevelType());
        assertNull(voting.getStartTime());
        assertNull(voting.getEndTime());
    }
}

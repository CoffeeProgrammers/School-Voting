package com.project.backend.mappers.petition;

import com.project.backend.dto.petition.PetitionFullResponse;
import com.project.backend.dto.petition.PetitionListResponse;
import com.project.backend.dto.petition.PetitionRequest;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
import com.project.backend.models.petition.Petition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PetitionMapperTest {

    private final PetitionMapper petitionMapper = Mappers.getMapper(PetitionMapper.class);

    private Petition petition;

    @BeforeEach
    void setUp() {
        petition = new Petition();
        petition.setId(1L);
        petition.setName("Save the Planet");
        petition.setDescription("An important cause.");
        petition.setLevelType(LevelType.SCHOOL);
        petition.setEndTime(LocalDateTime.of(2030, 1, 1, 12, 0));
        petition.setStatus(Status.ACTIVE);
        petition.setCountNeeded(100L);

        User creator = new User();
        creator.setId(10L);
        creator.setEmail("creator@example.com");
        creator.setFirstName("Jane");
        creator.setLastName("Doe");

        petition.setCreator(creator);
    }

    @Test
    void fromRequestToPetition_null() {
        assertNull(petitionMapper.fromRequestToPetition(null));
    }

    @Test
    void fromRequestToPetition_success() {
        PetitionRequest dto = new PetitionRequest();
        dto.setName("Ban Plastic");
        dto.setDescription("Protect the oceans.");
        dto.setLevelType("CLASS");

        Petition entity = petitionMapper.fromRequestToPetition(dto);

        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(LevelType.CLASS, entity.getLevelType());
    }


    @Test
    void fromRequestToPetition_levelTypeNull_safe() {
        PetitionRequest dto = new PetitionRequest();
        dto.setName("Ban Plastic");
        dto.setDescription("Protect the oceans.");
        dto.setLevelType(null);

        Petition entity = petitionMapper.fromRequestToPetition(dto);

        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(null, entity.getLevelType());
    }

    @Test
    void fromPetitionToFullResponse_null() {
        assertNull(petitionMapper.fromPetitionToFullResponse(null));
    }

    @Test
    void fromPetitionToFullResponse_success() {
        PetitionFullResponse dto = petitionMapper.fromPetitionToFullResponse(petition);

        assertNotNull(dto);
        assertEquals(petition.getId(), dto.getId());
        assertEquals(petition.getName(), dto.getName());
        assertEquals(petition.getDescription(), dto.getDescription());
        assertEquals(petition.getEndTime().toString().substring(0, 16), dto.getEndTime().substring(0, 16));
        assertEquals(petition.getLevelType().name(), dto.getLevelType());
        assertNotNull(dto.getCreator());
        assertEquals(petition.getCreator().getId(), dto.getCreator().getId());
        assertEquals(petition.getStatus().name(), dto.getStatus());
        assertEquals(petition.getCountNeeded(), dto.getCountNeeded());
    }

    @Test
    void fromPetitionToListResponse_null() {
        assertNull(petitionMapper.fromPetitionToListResponse(null));
    }

    @Test
    void fromPetitionToListResponse_success() {
        PetitionListResponse dto = petitionMapper.fromPetitionToListResponse(petition);

        assertNotNull(dto);
        assertEquals(petition.getId(), dto.getId());
        assertEquals(petition.getName(), dto.getName());
        assertEquals(petition.getEndTime().toString().substring(0, 16), dto.getEndTime().substring(0, 16));
        assertEquals(petition.getLevelType().name(), dto.getLevelType());
        assertNotNull(dto.getCreator());
        assertEquals(petition.getCreator().getId(), dto.getCreator().getId());
        assertEquals(petition.getStatus().name(), dto.getStatus());
        assertEquals(petition.getCountNeeded(), dto.getCountNeeded());
    }

    @Test
    void fromPetitionToFullResponse_nullCreator_safe() {
        petition.setCreator(null);
        PetitionFullResponse dto = petitionMapper.fromPetitionToFullResponse(petition);
        assertNotNull(dto);
        assertNull(dto.getCreator());
    }

    @Test
    void fromPetitionToListResponse_nullCreator_safe() {
        petition.setCreator(null);
        PetitionListResponse dto = petitionMapper.fromPetitionToListResponse(petition);
        assertNotNull(dto);
        assertNull(dto.getCreator());
    }

    @Test
    void fromPetitionToListResponse_allOptionalFieldsNull_safe() {
        Petition emptyPetition = new Petition();
        emptyPetition.setId(99L);
        emptyPetition.setName("Null Test");
        emptyPetition.setCountNeeded(50L);

        PetitionListResponse dto = petitionMapper.fromPetitionToListResponse(emptyPetition);

        assertNotNull(dto);
        assertEquals(99L, dto.getId());
        assertEquals("Null Test", dto.getName());
        assertEquals(50L, dto.getCountNeeded());

        assertNull(dto.getEndTime());
        assertNull(dto.getLevelType());
        assertNull(dto.getCreator());
        assertNull(dto.getStatus());
    }

    @Test
    void fromPetitionToFullResponse_allOptionalFieldsNull_safe() {
        Petition emptyPetition = new Petition();
        emptyPetition.setId(77L);
        emptyPetition.setName("Null Test");
        emptyPetition.setDescription("Empty fields");
        emptyPetition.setCountNeeded(10L);

        PetitionFullResponse dto = petitionMapper.fromPetitionToFullResponse(emptyPetition);

        assertNotNull(dto);
        assertEquals(77L, dto.getId());
        assertEquals("Null Test", dto.getName());
        assertEquals("Empty fields", dto.getDescription());
        assertEquals(10L, dto.getCountNeeded());

        assertNull(dto.getEndTime());
        assertNull(dto.getLevelType());
        assertNull(dto.getCreator());
        assertNull(dto.getStatus());
    }


}

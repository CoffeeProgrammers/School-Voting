package com.project.backend.dto.petition;

import com.project.backend.dto.user.UserListResponse;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetitionDtoTest {

    @Test
    void testPetitionRequest() {
        PetitionRequest dto1 = new PetitionRequest();
        dto1.setName("Name");
        dto1.setLevelType("LevelType");
        dto1.setDescription("Description");
        dto1.setLevelId(1L);

        PetitionRequest dto2 = new PetitionRequest();
        dto2.setName(dto1.getName());
        dto2.setLevelType(dto1.getLevelType());
        dto2.setDescription(dto1.getDescription());
        dto2.setLevelId(dto1.getLevelId());


        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testPetitionFullResponse() {
        PetitionFullResponse dto1 = new PetitionFullResponse();
        dto1.setName("Name");
        dto1.setLevelType("LevelType");
        dto1.setDescription("Description");
        dto1.setId(1L);
        dto1.setCreator(new UserListResponse());
        dto1.setEndTime(new Date().toString());
        dto1.setCountNeeded(10L);
        dto1.setCountSupported(1L);
        dto1.setStatus("Status");
        dto1.setSupportedByCurrentId(true);

        PetitionFullResponse dto2 = new PetitionFullResponse();

        dto2.setName(dto1.getName());
        dto2.setLevelType(dto1.getLevelType());
        dto2.setDescription(dto1.getDescription());
        dto2.setId(dto1.getId());
        dto2.setCreator(dto1.getCreator());
        dto2.setEndTime(dto1.getEndTime());
        dto2.setCountNeeded(dto1.getCountNeeded());
        dto2.setCountSupported(dto1.getCountSupported());
        dto2.setStatus(dto1.getStatus());
        dto2.setSupportedByCurrentId(dto1.getSupportedByCurrentId());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    void testPetitionListResponse(){
        PetitionListResponse dto1 = new PetitionListResponse();
        dto1.setName("Name");
        dto1.setLevelType("LevelType");
        dto1.setId(1L);
        dto1.setCreator(new UserListResponse());
        dto1.setEndTime(new Date().toString());
        dto1.setCountNeeded(10L);
        dto1.setCountSupported(1L);
        dto1.setStatus("Status");
        dto1.setSupportedByCurrentId(true);

        PetitionListResponse dto2 = new PetitionListResponse();

        dto2.setName(dto1.getName());
        dto2.setLevelType(dto1.getLevelType());
        dto2.setId(dto1.getId());
        dto2.setCreator(dto1.getCreator());
        dto2.setEndTime(dto1.getEndTime());
        dto2.setCountNeeded(dto1.getCountNeeded());
        dto2.setCountSupported(dto1.getCountSupported());
        dto2.setStatus(dto1.getStatus());
        dto2.setSupportedByCurrentId(dto1.getSupportedByCurrentId());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

}

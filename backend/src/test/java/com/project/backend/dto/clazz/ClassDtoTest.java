package com.project.backend.dto.clazz;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassDtoTest {

    @Test
    void testClassCreateRequest(){
        ClassCreateRequest dto1 = new ClassCreateRequest();
        dto1.setName("name");
        dto1.setUserIds(List.of(2L, 3L));

        ClassCreateRequest dto2 = new ClassCreateRequest();
        dto2.setName(dto1.getName());
        dto2.setUserIds(dto1.getUserIds());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testClassUpdateRequest(){
        ClassUpdateRequest dto1 = new ClassUpdateRequest();
        dto1.setName("name");

        ClassUpdateRequest dto2 = new ClassUpdateRequest();
        dto2.setName(dto1.getName());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testClassListResponse(){
        ClassResponse dto1 = new ClassResponse();
        dto1.setName("name");
        dto1.setId(1L);

        ClassResponse dto2 = new ClassResponse();
        dto2.setName(dto1.getName());
        dto2.setId(dto1.getId());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }
}

package com.project.backend.dto.clazz;

import com.project.backend.dto.user.UserListResponse;
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
    void testClassFullResponse(){
        ClassFullResponse dto1 = new ClassFullResponse();
        dto1.setName("name");
        dto1.setUsers(List.of(new UserListResponse()));
        dto1.setId(1L);

        ClassFullResponse dto2 = new ClassFullResponse();
        dto2.setName(dto1.getName());
        dto2.setUsers(dto1.getUsers());
        dto2.setId(dto1.getId());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testClassListResponse(){
        ClassListResponse dto1 = new ClassListResponse();
        dto1.setName("name");
        dto1.setId(1L);

        ClassListResponse dto2 = new ClassListResponse();
        dto2.setName(dto1.getName());
        dto2.setId(dto1.getId());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }
}

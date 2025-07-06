package com.project.backend.dto.school;

import com.project.backend.dto.user.DirectorCreateRequest;
import com.project.backend.dto.user.UserListResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchoolDtoTest {

    @Test
    void testSchoolResponse() {
        SchoolResponse dto1 = new SchoolResponse();
        dto1.setName("Name");
        dto1.setDirector(new UserListResponse());

        SchoolResponse dto2 = new SchoolResponse();
        dto2.setName(dto1.getName());
        dto2.setDirector(dto1.getDirector());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testSchoolCreateRequest() {
        SchoolCreateRequest dto1 = new SchoolCreateRequest();
        dto1.setName("Name");
        dto1.setDirector(new DirectorCreateRequest());

        SchoolCreateRequest dto2 = new SchoolCreateRequest();
        dto2.setName(dto1.getName());
        dto2.setDirector(dto1.getDirector());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }

    @Test
    void testSchoolUpdateRequest() {
        SchoolUpdateRequest dto1 = new SchoolUpdateRequest();
        dto1.setName("Name");

        SchoolUpdateRequest dto2 = new SchoolUpdateRequest();
        dto2.setName(dto1.getName());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto2.toString());
    }
}

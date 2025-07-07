package com.project.backend.dto.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDtoTest {

    @Test
    void testUserCreateRequest() {
        UserCreateRequest dto1 = new UserCreateRequest();
        dto1.setEmail("new@exmapl.com");
        dto1.setPassword("passWord1");
        dto1.setFirstName("Firstname");
        dto1.setLastName("Lastname");
        dto1.setRole("ROLE_USER");
        dto1.setEmailVerified(true);

        UserCreateRequest dto2 = new UserCreateRequest();
        dto2.setEmail(dto1.getEmail());
        dto2.setPassword(dto1.getPassword());
        dto2.setFirstName(dto1.getFirstName());
        dto2.setLastName(dto1.getLastName());
        dto2.setRole(dto1.getRole());
        dto2.setEmailVerified(dto1.getEmailVerified());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto1.toString());
    }

    @Test
    void testUserUpdateRequest() {
        UserUpdateRequest dto1 = new UserUpdateRequest();
        dto1.setFirstName("Firstname");
        dto1.setLastName("Lastname");

        UserUpdateRequest dto2 = new UserUpdateRequest();
        dto2.setFirstName(dto1.getFirstName());
        dto2.setLastName(dto1.getLastName());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto1.toString());

    }

    @Test
    void testDirectorCreateRequest() {
        DirectorCreateRequest dto1 = new DirectorCreateRequest();
        dto1.setEmail("new@exmapl.com");
        dto1.setPassword("passWord1");
        dto1.setFirstName("Firstname");
        dto1.setLastName("Lastname");

        DirectorCreateRequest dto2 = new DirectorCreateRequest();
        dto2.setEmail(dto1.getEmail());
        dto2.setPassword(dto1.getPassword());
        dto2.setFirstName(dto1.getFirstName());
        dto2.setLastName(dto1.getLastName());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto1.toString());
    }

    @Test
    void testUserFullResponse() {
        UserFullResponse dto1 = new UserFullResponse();
        dto1.setId(1L);
        dto1.setEmail("new@exmapl.com");
        dto1.setFirstName("Firstname");
        dto1.setLastName("Lastname");
        dto1.setRole("ROLE_USER");

        UserFullResponse dto2 = new UserFullResponse();
        dto2.setId(dto1.getId());
        dto2.setEmail(dto1.getEmail());
        dto2.setFirstName(dto1.getFirstName());
        dto2.setLastName(dto1.getLastName());
        dto2.setRole(dto1.getRole());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto1.toString());
    }

    @Test
    void testUserListResponse() {
        UserListResponse dto1 = new UserListResponse();
        dto1.setId(1L);
        dto1.setEmail("new@exmapl.com");
        dto1.setFirstName("Firstname");
        dto1.setLastName("Lastname");

        UserListResponse dto2 = new UserListResponse();
        dto2.setId(dto1.getId());
        dto2.setEmail(dto1.getEmail());
        dto2.setFirstName(dto1.getFirstName());
        dto2.setLastName(dto1.getLastName());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertEquals(dto1.toString(), dto1.toString());
    }
}

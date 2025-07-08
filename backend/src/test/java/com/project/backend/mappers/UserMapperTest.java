package com.project.backend.mappers;

import com.project.backend.TestUtil;
import com.project.backend.dto.user.*;
import com.project.backend.models.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private User user;

    @BeforeEach
    void setUp() {
        user = TestUtil.createUser("STUDENT", "student@gmail.com");
    }

    @Test
    void fromUserToFullResponse_null() {
        UserFullResponse dto = userMapper.fromUserToFullResponse(null);
        assertNull(dto);
    }

    @Test
    void fromUserToFullResponse_success() {
        UserFullResponse dto = userMapper.fromUserToFullResponse(user);

        assertNotNull(dto);
        assertEquals(user.getRole(), dto.getRole());
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
    }

    @Test
    void fromUserToListResponse_null() {
        UserListResponse dto = userMapper.fromUserToListResponse(null);
        assertNull(dto);
    }

    @Test
    void fromUserToListResponse_success() {
        UserListResponse dto = userMapper.fromUserToListResponse(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
    }

    @Test
    void fromCreateRequestToUser_null() {
        UserCreateRequest dto = null;
        User user1 = userMapper.fromRequestToUser(dto);
        assertNull(user1);
    }

    @Test
    void fromCreateRequestToUser_success() {
        UserCreateRequest dto = Instancio.create(UserCreateRequest.class);
        User user1 = userMapper.fromRequestToUser(dto);

        assertNotNull(user1);
        assertEquals(dto.getRole(), user1.getRole());
        assertEquals(dto.getEmail(), user1.getEmail());
        assertEquals(dto.getFirstName(), user1.getFirstName());
        assertEquals(dto.getLastName(), user1.getLastName());
    }

    @Test
    void fromUpdateRequestToUser_null() {
        UserUpdateRequest dto = null;
        User user1 = userMapper.fromRequestToUser(dto);
        assertNull(user1);
    }

    @Test
    void fromUpdateRequestToUser_success() {
        UserUpdateRequest dto = Instancio.create(UserUpdateRequest.class);
        User user1 = userMapper.fromRequestToUser(dto);

        assertNotNull(user1);
        assertEquals(dto.getFirstName(), user1.getFirstName());
        assertEquals(dto.getLastName(), user1.getLastName());
    }

    @Test
    void fromDirectorRequestToUser_null() {
        DirectorCreateRequest dto = null;
        User user1 = userMapper.fromDirectorRequestToUser(dto);
        assertNull(user1);
    }

    @Test
    void fromDirectorCreateRequestToUser_success() {
        DirectorCreateRequest dto = Instancio.create(DirectorCreateRequest.class);
        User user1 = userMapper.fromDirectorRequestToUser(dto);

        assertNotNull(user1);
        assertEquals(dto.getEmail(), user1.getEmail());
        assertEquals(dto.getFirstName(), user1.getFirstName());
        assertEquals(dto.getLastName(), user1.getLastName());
    }

}

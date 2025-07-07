package com.project.backend.mappers;

import com.project.backend.dto.clazz.ClassCreateRequest;
import com.project.backend.dto.clazz.ClassFullResponse;
import com.project.backend.dto.clazz.ClassListResponse;
import com.project.backend.dto.clazz.ClassUpdateRequest;
import com.project.backend.dto.user.UserListResponse;
import com.project.backend.models.Class;
import com.project.backend.models.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.project.backend.TestUtil.createUser;
import static org.junit.jupiter.api.Assertions.*;

public class ClassMapperTest {

    private final ClassMapper classMapper = Mappers.getMapper(ClassMapper.class);

    private Class classEntity;

    @BeforeEach
    void setUp() {
        classEntity = new Class();
        classEntity.setId(1L);
        classEntity.setName("10-A");
        classEntity.setUsers(null);
        classEntity.setSchool(null);
    }

    @Test
    void fromCreateRequestToClass_null() {
        Class result = classMapper.fromRequestToClass((ClassCreateRequest) null);
        assertNull(result);
    }

    @Test
    void fromCreateRequestToClass_success() {
        ClassCreateRequest dto = Instancio.create(ClassCreateRequest.class);
        Class result = classMapper.fromRequestToClass(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    void fromUpdateRequestToClass_null() {
        Class result = classMapper.fromRequestToClass((ClassUpdateRequest) null);
        assertNull(result);
    }

    @Test
    void fromUpdateRequestToClass_success() {
        ClassUpdateRequest dto = Instancio.create(ClassUpdateRequest.class);
        Class result = classMapper.fromRequestToClass(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    void fromClassToListResponse_null() {
        ClassListResponse dto = classMapper.fromClassToListResponse(null);
        assertNull(dto);
    }

    @Test
    void fromClassToListResponse_success() {
        ClassListResponse dto = classMapper.fromClassToListResponse(classEntity);

        assertNotNull(dto);
        assertEquals(classEntity.getId(), dto.getId());
        assertEquals(classEntity.getName(), dto.getName());
    }

    @Test
    void fromClassToFullResponse_null() {
        ClassFullResponse dto = classMapper.fromClassToFullResponse(null);
        assertNull(dto);
    }

    @Test
    void fromClassToFullResponse_success() {
        ClassFullResponse dto = classMapper.fromClassToFullResponse(classEntity);

        assertNotNull(dto);
        assertEquals(classEntity.getId(), dto.getId());
        assertEquals(classEntity.getName(), dto.getName());
    }

    @Test
    void fromClassToFullResponse_withUsers_success() {
        User user1 = createUser("STUDENT", "student@gmail.com");

        User user2 = createUser("TEACHER", "teacher@gmail.com");

        user1.setId(1);
        user2.setId(2);
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        users.add(null);

        classEntity.setUsers(users);

        ClassFullResponse dto = classMapper.fromClassToFullResponse(classEntity);

        assertNotNull(dto);
        assertEquals(classEntity.getId(), dto.getId());
        assertEquals(classEntity.getName(), dto.getName());
        assertNotNull(dto.getUsers());
        assertEquals(3, dto.getUsers().size());

        UserListResponse mappedUser = dto.getUsers().stream()
                .filter(Objects::nonNull)
                .filter(u -> u.getId().equals(1L))
                .findFirst()
                .orElse(null);

        assertNotNull(mappedUser);
        assertEquals(user1.getEmail(), mappedUser.getEmail());
        assertEquals(user1.getFirstName(), mappedUser.getFirstName());
        assertEquals(user1.getLastName(), mappedUser.getLastName());
    }

}

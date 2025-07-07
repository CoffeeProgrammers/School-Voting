package com.project.backend.mappers;

import com.project.backend.dto.clazz.ClassCreateRequest;
import com.project.backend.dto.clazz.ClassResponse;
import com.project.backend.dto.clazz.ClassUpdateRequest;
import com.project.backend.models.Class;
import com.project.backend.models.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
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
    void fromClassToResponse_null() {
        ClassResponse dto = classMapper.fromClassToResponse(null);
        assertNull(dto);
    }

    @Test
    void fromClassToResponse_success() {
        ClassResponse dto = classMapper.fromClassToResponse(classEntity);

        assertNotNull(dto);
        assertEquals(classEntity.getId(), dto.getId());
        assertEquals(classEntity.getName(), dto.getName());
    }

    @Test
    void fromClassToResponse_withUsers_success() {
        User user1 = createUser("STUDENT", "student@gmail.com");

        User user2 = createUser("TEACHER", "teacher@gmail.com");

        user1.setId(1);
        user2.setId(2);
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        users.add(null);

        classEntity.setUsers(users);

        ClassResponse dto = classMapper.fromClassToResponse(classEntity);

        assertNotNull(dto);
        assertEquals(classEntity.getId(), dto.getId());
        assertEquals(classEntity.getName(), dto.getName());
    }

}

package com.project.backend;

import com.project.backend.models.School;
import com.project.backend.models.User;
import org.instancio.Instancio;

import java.util.Arrays;

import static org.instancio.Select.field;

public class TestUtil {
    public static User createUser(String role, String email) {
        User user = Instancio.of(User.class).ignore(field("role")).ignore(field("email")).create();
        user.setRole(role);
        user.setEmail(email);
        return user;
    }

    public static User createUser(String role, String email, String keycloakUserId) {
        User user = createUser(role, email);
        user.setKeycloakUserId(keycloakUserId);
        return user;
    }

    public static School createSchool(String name) {
        School school = Instancio.of(School.class).ignore(field("name")).create();
        school.setName(name);
        return school;
    }

    public static School createSchool(String name, User director) {
        School school = createSchool(name);
        school.setDirector(director);
        return school;
    }

    public static void assignUsersToSchool(School school, User... users) {
        Arrays.stream(users).forEach(u -> u.setSchool(school));
    }
}

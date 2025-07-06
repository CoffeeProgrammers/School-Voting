package com.project.backend;

import com.project.backend.models.User;
import org.instancio.Instancio;

import static org.instancio.Select.field;

public class TestUtil {
    public static User createUser(String role, String email) {
        User user = Instancio.of(User.class).ignore(field("role")).ignore(field("email")).create();
        user.setRole(role);
        user.setEmail(email);
        return user;
    }
}

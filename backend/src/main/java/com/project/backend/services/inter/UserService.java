package com.project.backend.services.inter;

import com.project.backend.dto.wrapper.PasswordRequest;
import com.project.backend.models.User;
import org.keycloak.jose.jwk.JWK;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    User createUserKeycloak(User user, long schoolId);
    User createUser(User user, String password, long schoolId, String roleOfCreator);

    User updateUser(User user, long userId);
    User updateUserKeycloak(User user, long userId);

    boolean updatePassword(PasswordRequest passwordRequest, Authentication authentication);

    void delete(long id);
    User findById(long id);

    Page<User> findAllByVoting(long votingId, String email, String firstName, String lastName, int page, int size);

    Page<User> findAllByRoleInSchool(long schoolId, String role, String email, String firstName, String lastName, int page, int size);

    Page<User> findAllByClass(long classId, String email, String firstName, String lastName, int page, int size);

    Page<User> findAllStudentsWithoutClass(long schoolId, String email, String firstName, String lastName, int page, int size);

    List<User> findAllBySchool(long schoolId, long userId);

    List<User> findAllByClass(long classId, long userId);

    User findUserByAuth(Authentication authentication);
    User findUserByEmail(String email);

    User findUserByKeycloakUserId(String keycloakUserId);

    boolean isNotExistByEmail(String email);
}

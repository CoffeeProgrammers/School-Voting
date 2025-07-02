package com.project.backend.services.inter;

import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.dto.wrapper.PasswordRequest;
import com.project.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface UserService {
    User createUserKeycloak(User user);
    User createUser(User user, String password);

    User updateUser(User user, long userId);
    User updateUserKeycloak(User user, long userId);

    boolean updatePassword(PasswordRequest passwordRequest, Authentication authentication);

    void delete(long id);
    User findById(long id);
    Page<User> findAll(
            String email, String firstName, String lastName, String role,
            int page, int size, Authentication auth);
    User findUserByAuth(Authentication authentication);
    User findUserByEmail(String email);

    User findUserByKeycloakUserId(String keycloakUserId);

    boolean isNotExistByEmail(String email);
}

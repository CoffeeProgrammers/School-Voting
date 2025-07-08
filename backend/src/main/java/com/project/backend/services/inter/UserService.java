package com.project.backend.services.inter;

import com.project.backend.dto.wrapper.PasswordRequest;
import com.project.backend.models.Class;
import com.project.backend.models.User;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    User createUserKeycloak(User user, long schoolId);
    User createUser(User user, String password, long schoolId, String roleOfCreator, boolean emailVerified);

    User updateUser(User user, long userId);
    User updateUserKeycloak(User user, long userId);

    boolean updatePassword(PasswordRequest passwordRequest, User user);

    User findById(long id);

    Page<User> findAllByVoting(long votingId, String email, String firstName, String lastName, int page, int size);

    Page<User> findAllByRoleInSchool(long schoolId, String role, String email, String firstName, String lastName, int page, int size, long userId);

    Page<User> findAllByClass(long classId, String email, String firstName, String lastName, int page, int size);

    Page<User> findAllStudentsWithoutClass(long schoolId, String email, String firstName, String lastName, int page, int size);

    List<User> findAllBySchool(long schoolId, long userId);

    List<User> findAllByClass(long classId, long userId);

    List<User> findAllByClass(long classId);

    List<User> findAllBySchool(long schoolId);

    User findUserByAuth(Authentication authentication);

    void unassignClassFromUser(User user);

    User findUserByEmail(String email);

    User findUserByKeycloakUserId(String keycloakUserId);

    boolean isNotExistByEmail(String email);

    long countAllBySchoolAndRole(long schoolId, String role);

    long countAllByClass(long classId);

    void assignClassToUser(Class clazz, User user);

    User createDirector(User director, String password);

    void checkEmail(String email);

    User save(User user);

    List<User> findAllByPetition(Petition petition);

    List<User> findAllByVoting(Voting voting);
}

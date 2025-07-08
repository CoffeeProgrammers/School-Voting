package com.project.backend.services.impl;

import com.project.backend.dto.wrapper.PasswordRequest;
import com.project.backend.models.Class;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.repos.UserRepository;
import com.project.backend.repositories.specification.UserSpecification;
import com.project.backend.services.inter.SchoolService;
import com.project.backend.services.inter.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.project.backend.utils.SpecificationUtil.addSpecification;
import static com.project.backend.utils.SpecificationUtil.isValid;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RealmResource realmResource;
    private final String clientUUID;
    private final Map<String, RoleRepresentation> clientRoles;
    private final SchoolService schoolService;
    private final WebClient webClient;

    @Value("${realm}")
    private String realm;
    @Value("${client-id}")
    private String clientId;
    @Value("${client-secret}")
    private String clientSecret;

    @Override
    public User createUserKeycloak(User user, long schoolId) {
        log.info("Service: Saving new user from keycloak {}", user.getEmail());
        user.setSchool(schoolService.findById(schoolId));
        return userRepository.save(user);
    }

    @Override
    public User createUser(User user, String password, long schoolId, String roleOfCreator, boolean emailVerified) {
        log.info("Service: Saving new user from creating form {}", user.getEmail());
        if (roleOfCreator.equals("TEACHER") &&
                (user.getRole().equals("TEACHER") || user.getRole().equals("DIRECTOR"))) {
            throw new IllegalArgumentException("Teachers cant create teacher or director");
        }
        if (roleOfCreator.equals("DIRECTOR") &&
                user.getRole().equals("DIRECTOR")) {
            throw new IllegalArgumentException("DIRECTOR cant create director");
        }
        String email = user.getEmail();
        checkEmail(email);

        addUserToKeycloak(user, password, emailVerified);

        return createUserKeycloak(user, schoolId);
    }

    private void addUserToKeycloak(User user, String password, boolean emailVerified) {
        log.info("Service: Adding user {} to keycloak", user.getEmail());
        UserRepresentation userRepresentation = new UserRepresentation();

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEmailVerified(emailVerified);
        userRepresentation.setEnabled(true);

        Response response = realmResource.users().create(userRepresentation);
        if (response.getStatus() == 201) {
            log.info("Service: Successfully added user {} to keycloak", user.getEmail());
            URI location = response.getLocation();
            String path = location.getPath();
            String userId = path.substring(path.lastIndexOf('/') + 1);
            user.setKeycloakUserId(userId);
        } else {
            log.warn("Service: Failed to create user. Status: " + response.getStatus());
            String error = response.readEntity(String.class);
            log.error("Service: Error response: " + error);
            throw new ResponseStatusException(HttpStatusCode.valueOf(response.getStatus()), error);
        }
        response.close();

        realmResource.users()
                .get(user.getKeycloakUserId())
                .roles()
                .clientLevel(clientUUID)
                .add(List.of(clientRoles.get(user.getRole())));
    }

    @Override
    public User updateUser(User newUser, long userId) {
        log.info("Service: Updating user with id {}", userId);

        User userToUpdate = findById(userId);
        checkForDeletedUser(userToUpdate);

        String newFirstName = newUser.getFirstName();
        String newLastName = newUser.getLastName();

        String keycloakUserId = userToUpdate.getKeycloakUserId();

        userToUpdate.setFirstName(newFirstName);
        userToUpdate.setLastName(newLastName);

        UserRepresentation userRepresentation = realmResource.users().get(keycloakUserId).toRepresentation();
        userRepresentation.setFirstName(newFirstName);
        userRepresentation.setLastName(newLastName);

        realmResource.users().get(keycloakUserId).update(userRepresentation);

        return userRepository.save(userToUpdate);
    }

    @Override
    public User updateUserKeycloak(User newUser, long userId) {
        log.info("Service: Updating user with id {} from keycloak", userId);

        User userToUpdate = findById(userId);
        checkForDeletedUser(userToUpdate);

        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setLastName(newUser.getLastName());
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setRole(newUser.getRole());

        return userRepository.save(userToUpdate);
    }

    private boolean verifyOldPassword(String username, String password) {
        log.info("Service: Verifying old password for user {}", username);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password);

        try {
            webClient.post()
                    .uri("/realms/" + realm + "/protocol/openid-connect/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Service: Successfully verified old password for user {}", username);
            return true;
        } catch (WebClientResponseException e) {
            log.warn("Service: Failed to verify old password for user {}", username, e);
            return false;
        }
    }

    @Override
    public boolean updatePassword(PasswordRequest passwordRequest, User user) {
        String keycloakUserId = user.getKeycloakUserId();
        String email = user.getEmail();

        log.info("Service: Updating password for user with email {}", email);

        boolean isOldPasswordValid = verifyOldPassword(email, passwordRequest.getOldPassword());
        if (!isOldPasswordValid) {
            log.warn("Service: Old password is incorrect for user {}", email);
            throw new IllegalArgumentException("Old password is incorrect");
        }

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(passwordRequest.getNewPassword());
        credential.setTemporary(false);

        realmResource.users().get(keycloakUserId).resetPassword(credential);

        log.info("Password successfully updated for user {}", email);
        return true;
    }

    @Override
    public User findById(long id) {
        log.info("Service: Finding user with id {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public Page<User> findAllByVoting(long votingId, String email, String firstName, String lastName, int page, int size) {
        log.info("Service: Finding all users by voting with id {} and filters", votingId);
        Specification<User> userSpecification = createSpecification(email, firstName, lastName, null);
        Specification<User> fullSpecification = userSpecification == null ? UserSpecification.usersByVoting(votingId) : userSpecification.and(UserSpecification.usersByVoting(votingId));
        return userRepository.findAll(
                fullSpecification,
                PageRequest.of(
                        page, size
                )
        );
    }

    @Override
    public Page<User> findAllByRoleInSchool(long schoolId, String role, String email, String firstName, String lastName, int page, int size, long userId) {
        log.info("Service: Finding all users by role {} and filters", role);
        Specification<User> userSpecification = createSpecification(email, firstName, lastName, role);
        Specification<User> fullSpecification = userSpecification == null ?
                UserSpecification.bySchool(schoolId).and(UserSpecification.notUser(userId)) :
                userSpecification.and(UserSpecification.bySchool(schoolId).and(UserSpecification.notUser(userId)));
        return userRepository.findAll(
                fullSpecification,
                PageRequest.of(
                        page, size, Sort.by(Sort.Direction.ASC, "lastName", "firstName")
                )
        );
    }

    @Override
    public Page<User> findAllByClass(long classId, String email, String firstName, String lastName, int page, int size) {
        log.info("Service: Finding all users by class {} and filters", classId);
        Specification<User> userSpecification = createSpecification(email, firstName, lastName, null);
        Specification<User> fullSpecification = userSpecification == null ? UserSpecification.byClass(classId) : userSpecification.and(UserSpecification.byClass(classId));
        return userRepository.findAll(
                fullSpecification,
                PageRequest.of(
                        page, size, Sort.by(Sort.Direction.ASC, "lastName", "firstName")
                )
        );
    }

    @Override
    public Page<User> findAllStudentsWithoutClass(long schoolId, String email, String firstName, String lastName, int page, int size) {
        log.info("Service: Finding all users without class and filters");
        Specification<User> userSpecification = createSpecification(email, firstName, lastName, "STUDENT");
        Specification<User> fullSpecification = userSpecification == null ?
                UserSpecification.notInAnyClass().and(UserSpecification.bySchool(schoolId)) : userSpecification.and(UserSpecification.notInAnyClass()).and(UserSpecification.bySchool(schoolId));
        return userRepository.findAll(
                fullSpecification,
                PageRequest.of(
                        page, size, Sort.by(Sort.Direction.ASC, "lastName", "firstName")
                )
        );
    }

    @Override
    public List<User> findAllBySchool(long schoolId, long userId) {
        log.info("Service: Finding list of all users by school {} excluding user with id {}", schoolId, userId);
        return userRepository.findAll(UserSpecification.bySchool(schoolId).and(UserSpecification.notUser(userId)));
    }

    @Override
    public List<User> findAllBySchool(long schoolId) {
        log.info("Service: Finding list of all users by school {}", schoolId);
        return userRepository.findAll(UserSpecification.bySchool(schoolId));
    }

    @Override
    public List<User> findAllByClass(long classId, long userId) {
        log.info("Service: Finding list of all users by class {} excluding user with id {}", classId, userId);
        return userRepository.findAll(UserSpecification.byClass(classId).and(UserSpecification.notUser(userId)));
    }

    @Override
    public List<User> findAllByClass(long classId) {
        log.info("Service: Finding list of all users by class {}", classId);
        return userRepository.findAll(UserSpecification.byClass(classId));
    }

    @Override
    public User findUserByAuth(Authentication authentication) {
        log.info("Service: Finding user by authentication");
        return findUserByKeycloakUserId(authentication.getName());
    }

    @Override
    public User findUserByKeycloakUserId(String keycloakUserId) {
        log.info("Service: Finding user by keycloakUserId {}", keycloakUserId);
        return userRepository.findByKeycloakUserId(keycloakUserId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public boolean isNotExistByEmail(String email) {
        log.info("Service: Checking if user with email {} exist", email);
        return !userRepository.existsByEmail(email);
    }

    @Override
    public long countAllBySchoolAndRole(long schoolId, String role) {
        log.info("Service: Counting all users by school {} and role {}", schoolId, role);
        return userRepository.countAllBySchool_IdAndRoleIs(schoolId, role);
    }

    @Override
    public long countAllByClass(long classId) {
        log.info("Service: Counting all users by class {}", classId);
        return userRepository.countAllByMyClass_Id(classId);
    }

    @Override
    public void assignClassToUser(Class clazz, User user) {
        log.info("Service: Assigning user {} to class {}", user.getEmail(), clazz.getName());
        user.setMyClass(clazz);
        userRepository.save(user);
    }

    @Override
    public User createDirector(User director, String password) {
        log.info("Service: Creating director {}", director);
        addUserToKeycloak(director, password, true);
        User user = userRepository.save(director);
        return user;
    }

    @Override
    public void unassignClassFromUser(User user) {
        log.info("Service: Unassigning director {}", user.getEmail());
        user.setMyClass(null);
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        log.info("Service: Finding user by email {}", email);
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found with email " + email));
    }

    @Override
    public void checkEmail(String email) {
        log.info("Service: Checking if user with email {}", email);
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("User with email " + email + " already exists");
        }
    }

    @Override
    public User save(User user) {
        log.info("Service: Saving user {}", user);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllByPetition(Petition petition){
        log.info("Service: Finding all users by petition {}", petition);
        if(petition.getLevelType() == LevelType.SCHOOL){
            return userRepository.findAll(UserSpecification.bySchool(petition.getCreator().getSchool().getId()).and(UserSpecification.connectedToGoogle()));
        } else if (petition.getLevelType() == LevelType.CLASS) {
            return userRepository.findAll(UserSpecification.byClass(petition.getTargetId()).and(UserSpecification.connectedToGoogle()));
        }
        return List.of();
    }

    @Override
    public List<User> findAllByVoting(Voting voting){
        log.info("Service: Finding all users by voting {}", voting);
        return userRepository.findAll(
                UserSpecification.byVoting(voting).and(UserSpecification.connectedToGoogle()));
    }

    private Specification<User> createSpecification(String email, String firstName, String lastName, String role) {
        log.info("Service: Creating specification for user with email {}, first name {}, last name {} and role {}", email, firstName, lastName, role);
        Specification<User> specification = null;

        if (isValid(email)) {
            specification = addSpecification(specification, UserSpecification::byEmail, email);
        }
        if (isValid(firstName)) {
            specification = addSpecification(specification, UserSpecification::byFirstName, firstName);
        }
        if (isValid(lastName)) {
            specification = addSpecification(specification, UserSpecification::byLastName, lastName);
        }
        if (isValid(role)) {
            specification = addSpecification(specification, UserSpecification::byRole, role);
        }
        return specification;
    }

    private void checkForDeletedUser(User user) {
        log.info("Service: Checking for deleted user with id {}", user.getId());

        if ("!deleted-user!@deleted.com".equals(user.getEmail())) {
            log.info("Service: User with id {} is deleted", user.getId());
            throw new EntityExistsException("Can`t do anything with deleted user");
        }
    }
}

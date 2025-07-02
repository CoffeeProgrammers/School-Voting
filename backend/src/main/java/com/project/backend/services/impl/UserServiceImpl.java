package com.project.backend.services.impl;

import com.project.backend.dto.wrapper.PasswordRequest;
import com.project.backend.models.User;
import com.project.backend.repositories.UserRepository;
import com.project.backend.repositories.specification.UserSpecification;
import com.project.backend.services.inter.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RealmResource realmResource;
    private final ClientResource clientResource;
    private final String clientUUID;
    private final Map<String, RoleRepresentation> clientRoles;

    private final WebClient webClient;

    @Value("${realm}")
    private String realm;
    @Value("${client-id}")
    private String clientId;
    @Value("${client-secret}")
    private String clientSecret;

    @Override
    public User createUserKeycloak(User user) {
        log.info("Service: Saving new user {}", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User createUser(User user, String password) {
        log.info("Service: Saving new user {}", user.getEmail());
        String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("User with email " + email + " already exists");
        }

        UserRepresentation userRepresentation = new UserRepresentation();

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);

        Response response = realmResource.users().create(userRepresentation);
        if (response.getStatus() == 201) {
            URI location = response.getLocation();
            String path = location.getPath();
            String userId = path.substring(path.lastIndexOf('/') + 1);
            user.setKeycloakUserId(userId);
        } else {
            log.info("Service: Failed to create user. Status: " + response.getStatus());
            String error = response.readEntity(String.class);
            log.info("Service: Error response: " + error);
        }
        response.close();

        realmResource.users()
                .get(user.getKeycloakUserId())
                .roles()
                .clientLevel(clientUUID)
                .add(List.of(clientRoles.get(user.getRole())));

        return createUserKeycloak(user);
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
        log.info("Service: Updating user with id {}", userId);

        User userToUpdate = findById(userId);
        checkForDeletedUser(userToUpdate);

        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setLastName(newUser.getLastName());
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setRole(newUser.getRole());

        return userRepository.save(userToUpdate);
    }

    private boolean verifyOldPassword(String username, String password) {
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

            return true;
        } catch (WebClientResponseException e) {
            return false;
        }
    }

    @Override
    public boolean updatePassword(PasswordRequest passwordRequest, Authentication authentication) {
        String keycloakUserId = authentication.getName();
        String email = findUserByKeycloakUserId(keycloakUserId).getEmail();

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
    public void delete(long id) {
        log.info("Service: Deleting user with id {}", id);
        User user = findById(id);
        checkForDeletedUser(user);

        realmResource.users().delete(user.getKeycloakUserId());
        userRepository.deleteById(id);
    }

    @Override
    public User findById(long id) {
        log.info("Service: Finding user with id {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public Page<User> findAllByVoting(long votingId, String email, String firstName, String lastName, String role, int page, int size, Authentication auth) {
        return userRepository.findAll(
                createSpecification(email, firstName, lastName, null)
                        .and(UserSpecification.usersByVotingSortedByAnswer(votingId)),
                PageRequest.of(
                        page, size
                )
        );
    }

    @Override
    public Page<User> findAllByRole(String role, String email, String firstName, String lastName, int page, int size, Authentication auth) {
        return userRepository.findAll(
                createSpecification(email, firstName, lastName, role),
                PageRequest.of(
                        page, size, Sort.by(Sort.Direction.ASC, "lastName", "firstName")
                )
        );
    }

    @Override
    public Page<User> findAllByClass(long classId, String email, String firstName, String lastName, int page, int size, Authentication auth) {
        return userRepository.findAll(
                createSpecification(email, firstName, lastName, null)
                        .and(UserSpecification.byClass(classId)),
                PageRequest.of(
                        page, size, Sort.by(Sort.Direction.ASC, "lastName", "firstName")
                )
        );
    }

    @Override
    public User findUserByAuth(Authentication authentication) {
        log.info("Service: Finding user by authentication {}", authentication);
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
        return !userRepository.existsByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) {
        log.info("Service: Finding user by email {}", email);
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found with email " + email));
    }

    private boolean isValid(String value) {
        return value != null && !value.isBlank() && !value.equals("null");
    }

    private Specification<User> createSpecification(String email, String firstName, String lastName, String role) {
        Specification<User> specification = null;

        if (isValid(email)) {
            specification = (specification == null)
                    ? UserSpecification.byEmail(email)
                    : specification.and(UserSpecification.byEmail(email));
        }
        if (isValid(firstName)) {
            specification = (specification == null)
                    ? UserSpecification.byFirstName(firstName)
                    : specification.and(UserSpecification.byFirstName(firstName));
        }
        if (isValid(lastName)) {
            specification = (specification == null)
                    ? UserSpecification.byLastName(lastName)
                    : specification.and(UserSpecification.byLastName(lastName));
        }
        if (isValid(role)) {
            specification = (specification == null)
                    ? UserSpecification.byRole(role)
                    : specification.and(UserSpecification.byRole(role));
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

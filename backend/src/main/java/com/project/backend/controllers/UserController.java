package com.project.backend.controllers;

import com.project.backend.dto.user.UserCreateRequest;
import com.project.backend.dto.user.UserFullResponse;
import com.project.backend.dto.user.UserListResponse;
import com.project.backend.dto.user.UserUpdateRequest;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.mappers.UserMapper;
import com.project.backend.models.User;
import com.project.backend.services.inter.UserDeletionService;
import com.project.backend.services.inter.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/schools/{school_id}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final UserDeletionService userDeletionService;

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullResponse createUser(@PathVariable(value = "school_id") long schoolId,
                                       @Valid @RequestBody UserCreateRequest request,
                                       Authentication auth) {
        log.info("Controller: Create user with body: {}", request);
        User user = userMapper.fromRequestToUser(request);
        return userMapper.fromUserToFullResponse(userService.createUser(user, request.getPassword(),
                schoolId, userService.findUserByAuth(auth).getRole()));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) " +
            "and (hasAnyRole('DIRECTOR', 'TEACHER') or @userSecurity.checkUser(#auth, #userId))")
    @PutMapping("/update/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public UserFullResponse updateUser(
            @PathVariable(value = "school_id") long schoolId,
            @PathVariable(value = "user_id") long userId,
            @Valid @RequestBody UserUpdateRequest request,
            Authentication auth) {
        log.info("Controller: Update user with id: {} with body: {}", userId, request);
        User user = userMapper.fromRequestToUser(request);
        return userMapper.fromUserToFullResponse(userService.updateUser(user, userId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @DeleteMapping("/delete/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "school_id") long schoolId,
                           @PathVariable(value = "user_id") long userId,
                           Authentication auth) {
        log.info("Controller: Delete user with id: {}", userId);
        User user = userService.findById(userId);
        userDeletionService.delete(user, false);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public UserFullResponse getMyUser(@PathVariable(value = "school_id") long schoolId, Authentication auth) {
        log.info("Controller: Get my user");
        return userMapper.fromUserToFullResponse(userService.findUserByAuth(auth));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and @userSecurity.checkUserVoting(#auth, #votingId)")
    @GetMapping("/voting/{voting_id}")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<UserListResponse> getUsersByVoting(
            @PathVariable(value = "school_id") long schoolId,
            @PathVariable(value = "voting_id") long votingId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth) {
        log.info("Controller: Get users by voting {}", votingId);
        Page<User> userPage = userService.findAllByVoting(votingId, email, firstName, lastName, page, size);
        PaginationListResponse<UserListResponse> userListResponse = new PaginationListResponse<>();
        userListResponse.setContent(userPage.getContent().stream().map(userMapper::fromUserToListResponse).toList());
        userListResponse.setTotalPages(userPage.getTotalPages());
        return userListResponse;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping("/role/{role}")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<UserListResponse> getUsersByRole(
            @PathVariable(value = "school_id") long schoolId,
            @PathVariable(value = "role") String role,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth) {
        log.info("Controller: Get users by role {}", role);
        Page<User> userPage = userService.findAllByRoleInSchool(schoolId, role, email, firstName, lastName, page, size);
        PaginationListResponse<UserListResponse> userListResponse = new PaginationListResponse<>();
        userListResponse.setContent(userPage.getContent().stream().map(userMapper::fromUserToListResponse).toList());
        userListResponse.setTotalPages(userPage.getTotalPages());
        return userListResponse;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and (hasAnyRole('DIRECTOR', 'TEACHER') or @userSecurity.checkUserClass(#auth, #classId))")
    @GetMapping("/class/{class_id}")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<UserListResponse> getUsersByClass(
            @PathVariable(value = "school_id") long schoolId,
            @PathVariable(value = "class_id") long classId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth) {
        log.info("Controller: Get users by class with id: {}", classId);
        Page<User> userPage = userService.findAllByClass(classId, email, firstName, lastName, page, size);
        PaginationListResponse<UserListResponse> userListResponse = new PaginationListResponse<>();
        userListResponse.setContent(userPage.getContent().stream().map(userMapper::fromUserToListResponse).toList());
        userListResponse.setTotalPages(userPage.getTotalPages());
        return userListResponse;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping("/withoutClass")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<UserListResponse> getUsersWithoutClass(
            @PathVariable(value = "school_id") long schoolId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth) {
        log.info("Controller: Get users without class");
        Page<User> userPage = userService.findAllStudentsWithoutClass(schoolId, email, firstName, lastName, page, size);
        PaginationListResponse<UserListResponse> userListResponse = new PaginationListResponse<>();
        userListResponse.setContent(userPage.getContent().stream().map(userMapper::fromUserToListResponse).toList());
        userListResponse.setTotalPages(userPage.getTotalPages());
        return userListResponse;
    }
}
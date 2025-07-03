package com.project.backend.controllers;

import com.project.backend.dto.user.UserCreateRequest;
import com.project.backend.dto.user.UserFullResponse;
import com.project.backend.dto.user.UserListResponse;
import com.project.backend.dto.user.UserUpdateRequest;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.dto.wrapper.PasswordRequest;
import com.project.backend.mappers.UserMapper;
import com.project.backend.models.User;
import com.project.backend.services.inter.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
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

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasAnyRole('DIRECTOR', 'TEACHER')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullResponse createUser(@PathVariable(value = "school_id") long schoolId,
                                       @Valid @RequestBody UserCreateRequest request){
        log.info("Controller: Create user with body: {}", request);
        User user = userMapper.fromRequestToUser(request);
        return userMapper.fromUserToFullResponse(userService.createUser(user, request.getPassword(), schoolId));
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'TEACHER')")
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserFullResponse updateUser(
            @PathVariable(value = "school_id") long schoolId,
            @PathVariable long id,
            @Valid @RequestBody UserUpdateRequest request) {
        log.info("Controller: Update user with id: {} with body: {}", id, request);
        User user = userMapper.fromRequestToUser(request);
        return userMapper.fromUserToFullResponse(userService.updateUser(user, id));
    }

    @PutMapping("/update/password")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateMyPassword(
            @PathVariable(value = "school_id") long schoolId,
            @RequestBody PasswordRequest password,
            Authentication auth) {
        log.info("Controller: Update my password");
        return userService.updatePassword(password, auth);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'TEACHER')")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "school_id") long schoolId, @PathVariable long id) {
        log.info("Controller: Delete user with id: {}", id);
        userService.delete(id);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public UserFullResponse getMyUser(@PathVariable(value = "school_id") long schoolId, Authentication auth) {
        log.info("Controller: Get my user");
        return userMapper.fromUserToFullResponse(userService.findUserByAuth(auth));
    }

    @PreAuthorize("@userSecurity.checkUserVoting(#auth, #votingId)")
    @GetMapping("/voting/{voting_id}")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<UserListResponse> getUsersByVoting(
            @PathVariable(value = "school_id") long schoolId,
            @PathVariable(value = "voting_id") long votingId,
            @PathParam("email") String email,
            @PathParam("firstName")  String firstName,
            @PathParam("lastName")   String lastName,
            @PathParam("page") int page,
            @PathParam("size") int size,
            Authentication auth) {
        log.info("Controller: Get users by voting {}", votingId);
        Page<User> userPage = userService.findAllByVoting(votingId, email, firstName, lastName, page, size);
        PaginationListResponse<UserListResponse> userListResponse = new PaginationListResponse<>();
        userListResponse.setContent(userPage.getContent().stream().map(userMapper::fromUserToListResponse).toList());
        userListResponse.setTotalPages(userPage.getTotalPages());
        return userListResponse;
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping("/role/{role}")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<UserListResponse> getUsersByRole(
            @PathVariable(value = "school_id") long schoolId,
            @PathVariable(value = "role") String role,
            @PathParam("email") String email,
            @PathParam("firstName")  String firstName,
            @PathParam("lastName")   String lastName,
            @PathParam("page") int page,
            @PathParam("size") int size,
            Authentication auth) {
        log.info("Controller: Get users by role {}", role);
        Page<User> userPage = userService.findAllByRoleInSchool(schoolId, role, email, firstName, lastName, page, size);
        PaginationListResponse<UserListResponse> userListResponse = new PaginationListResponse<>();
        userListResponse.setContent(userPage.getContent().stream().map(userMapper::fromUserToListResponse).toList());
        userListResponse.setTotalPages(userPage.getTotalPages());
        return userListResponse;
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping("/class/{class_id}")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<UserListResponse> getUsersByClass(
            @PathVariable(value = "school_id") long schoolId,
            @PathVariable(value = "class_id") long classId,
            @PathParam("email") String email,
            @PathParam("firstName")  String firstName,
            @PathParam("lastName")   String lastName,
            @PathParam("page") int page,
            @PathParam("size") int size,
            Authentication auth) {
        log.info("Controller: Get users by class with id: {}", classId);
        Page<User> userPage = userService.findAllByClass(classId, email, firstName, lastName, page, size);
        PaginationListResponse<UserListResponse> userListResponse = new PaginationListResponse<>();
        userListResponse.setContent(userPage.getContent().stream().map(userMapper::fromUserToListResponse).toList());
        userListResponse.setTotalPages(userPage.getTotalPages());
        return userListResponse;
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping("/without-class")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<UserListResponse> getUsersWithoutClass(
            @PathVariable(value = "school_id") long schoolId,
            @PathParam("email") String email,
            @PathParam("firstName")  String firstName,
            @PathParam("lastName")   String lastName,
            @PathParam("page") int page,
            @PathParam("size") int size,
            Authentication auth) {
        log.info("Controller: Get users without class");
        Page<User> userPage = userService.findAllStudentsWithoutClass(schoolId, email, firstName, lastName, page, size);
        PaginationListResponse<UserListResponse> userListResponse = new PaginationListResponse<>();
        userListResponse.setContent(userPage.getContent().stream().map(userMapper::fromUserToListResponse).toList());
        userListResponse.setTotalPages(userPage.getTotalPages());
        return userListResponse;
    }
}
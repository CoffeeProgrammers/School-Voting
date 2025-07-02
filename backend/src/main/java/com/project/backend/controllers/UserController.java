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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("Controller: Create user with body: {}", request);
        User user = userMapper.fromRequestToUser(request);
        return userMapper.fromUserToFullResponse(userService.createUser(user, request.getPassword()));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserFullResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        log.info("Controller: Update user with id: {} with body: {}", id, request);
        User user = userMapper.fromRequestToUser(request);
        return userMapper.fromUserToFullResponse(userService.updateUser(user, id));
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UserFullResponse updateMyUser(
            @RequestBody UserUpdateRequest request,
            Authentication auth) {
        log.info("Controller: Update my user with body: {}", request);
        long userId = userService.findUserByAuth(auth).getId();
        User user = userMapper.fromRequestToUser(request);
        return userMapper.fromUserToFullResponse(userService.updateUser(user, userId));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Controller: Delete user with id: {}", id);
        userService.delete(id);
    }

    @PutMapping("/update/password")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateMyPassword(
            @RequestBody PasswordRequest password,
            Authentication auth) {
        log.info("Controller: Update my password");
        return userService.updatePassword(password, auth);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserFullResponse getUser(@PathVariable Long id) {
        log.info("Controller: Get user with id: {}", id);
        return userMapper.fromUserToFullResponse(userService.findById(id));
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public UserFullResponse getMyUser(Authentication auth) {
        log.info("Controller: Get my user");
        return userMapper.fromUserToFullResponse(userService.findUserByAuth(auth));
    }
}
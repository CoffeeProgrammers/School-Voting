package com.project.backend.auth.config;

import com.project.backend.models.School;
import com.project.backend.services.inter.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final UserService userService;

    private boolean checkUser(Authentication authentication, String username) {
        log.info("preAuth: Checking user {}", username);
        String authenticatedUserEmail = userService.findUserByAuth(authentication).getEmail();
        return authenticatedUserEmail.equals(username);
    }

    private boolean checkUserSchool(Authentication authentication, long schoolId) {
        log.info("preAuth: Checking if user in school {}", schoolId);
        School school = userService.findUserByAuth(authentication).getSchool();
        return school.getId() == schoolId;
    }
}

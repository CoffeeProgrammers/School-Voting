package com.project.backend.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchoolAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final UserSecurity userSecurity;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        Authentication auth = authentication.get();

        String[] segments = request.getRequestURI().split("/");
        Long schoolId = null;
        for (int i = 0; i < segments.length - 1; i++) {
            if (segments[i].equalsIgnoreCase("school")) {
                try {
                    schoolId = Long.parseLong(segments[i + 1]);
                    break;
                } catch (NumberFormatException ignored) {}
            }
        }

        if (schoolId != null && userSecurity.checkUserSchool(auth, schoolId)) {
            return new AuthorizationDecision(true);
        }

        log.warn("Access denied: schoolId mismatch or missing.");
        return new AuthorizationDecision(false);
    }
}

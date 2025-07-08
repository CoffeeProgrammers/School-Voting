package com.project.backend.auth.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityUtilTest {

    private String clientId = "CLIENT-ID";
    @Test
    void extractRole_validStructure_returnsFirstRole() {
        Jwt jwt = mock(Jwt.class);
        Map<String, Object> clientAccess = Map.of("roles", List.of("TEACHER"));
        Map<String, Object> resourceAccess = Map.of(clientId, clientAccess);

        when(jwt.getClaim("resource_access")).thenReturn(resourceAccess);

        String role = SecurityUtil.extractRole(clientId, jwt);

        assertEquals("TEACHER", role);
    }

    @Test
    void extractRole_missingResourceAccess_returnsEmptyString() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("resource_access")).thenReturn(null);

        String role = SecurityUtil.extractRole(clientId, jwt);

        assertEquals("", role);
    }

    @Test
    void extractRole_missingClientKey_returnsEmptyString() {
        Jwt jwt = mock(Jwt.class);
        Map<String, Object> resourceAccess = Map.of("other-client", Map.of("roles", List.of("DIRECTOR")));

        when(jwt.getClaim("resource_access")).thenReturn(resourceAccess);

        String role = SecurityUtil.extractRole(clientId, jwt);

        assertEquals("", role);
    }

    @Test
    void extractRole_rolesKeyMissing_returnsEmptyString() {
        Jwt jwt = mock(Jwt.class);
        Map<String, Object> clientAccess = Map.of();
        Map<String, Object> resourceAccess = Map.of(clientId, clientAccess);

        when(jwt.getClaim("resource_access")).thenReturn(resourceAccess);

        String role = SecurityUtil.extractRole(clientId, jwt);

        assertEquals("", role);
    }

    @Test
    void extractRole_emptyRoleList_returnsEmptyString() {
        Jwt jwt = mock(Jwt.class);
        Map<String, Object> clientAccess = Map.of("roles", List.of());
        Map<String, Object> resourceAccess = Map.of(clientId, clientAccess);

        when(jwt.getClaim("resource_access")).thenReturn(resourceAccess);

        String role = SecurityUtil.extractRole(clientId, jwt);

        assertEquals("", role);
    }

    @Test
    void testConstructor() {
        SecurityUtil securityUtil = new SecurityUtil();
        assertEquals(SecurityUtil.class, securityUtil.getClass());
    }
}

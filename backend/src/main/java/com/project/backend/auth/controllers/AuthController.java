package com.project.backend.auth.controllers;

import com.project.backend.dto.wrapper.PasswordRequest;
import com.project.backend.models.User;
import com.project.backend.services.inter.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

import static com.project.backend.auth.utils.CookieUtil.createCookie;
import static com.project.backend.auth.utils.CookieUtil.deleteCookie;
import static com.project.backend.auth.utils.SecurityUtil.extractRole;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final WebClient webClient;

    @Value("${realm}")
    private String realm;
    @Value("${client-secret}")
    private String clientSecret;
    @Value("${client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.coffee-programmers-client.redirect-uri}")
    private String redirectUri;
    private final UserService userService;
    private final JwtDecoder jwtDecoder;
    private final RealmResource realmResource;


    @PutMapping("/update/password")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateMyPassword(
            @RequestBody PasswordRequest password,
            Authentication auth) {
        log.info("Controller: Update my password");
        return userService.updatePassword(password, userService.findUserByAuth(auth));
    }

    @PostMapping("/callback")
    public ResponseEntity<Void> exchangeCode(@RequestParam String code) {
        log.info(code);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);
        formData.add("client_id", clientId);
        if (clientSecret != null) {
            formData.add("client_secret", clientSecret);
        }

        return requestToken(formData);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@RequestParam String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);
        formData.add("client_id", clientId);
        if (clientSecret != null) {
            formData.add("client_secret", clientSecret);
        }

        return requestToken(formData);
    }

    private ResponseEntity<Void> requestToken(MultiValueMap<String, String> formData) {
        try {
            return handleResponse(
                    webClient.post()
                            .uri("/realms/" + realm + "/protocol/openid-connect/token")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .bodyValue(formData)
                            .retrieve()
                            .toEntity(Map.class)
                            .block()
                            .getBody()
            );
        } catch (WebClientResponseException e) {
            log.error("Controller: {} Error response from Keycloak: {}", e.getRawStatusCode(), e.getResponseBodyAsString());

            return ResponseEntity.status(e.getRawStatusCode()).build();
        }
    }


    private ResponseEntity<Void> handleResponse(Map response) {
        String accessTokenString = (String) response.get("access_token");
        String refreshTokenString = (String) response.get("refresh_token");
        String idTokenString = (String) response.get("id_token");

        Jwt accessToken = jwtDecoder.decode(accessTokenString);
        Jwt idToken = jwtDecoder.decode(idTokenString);

        String email = idToken.getClaim("email");
        String role = extractRole(clientId, accessToken);

        User user = new User();
        user.setEmail(email);
        user.setFirstName(idToken.getClaim("given_name"));
        user.setLastName(idToken.getClaim("family_name"));
        user.setKeycloakUserId(idToken.getClaim("sub"));
        user.setRole(role);

        if (userService.isNotExistByEmail(email)) {
            user = userService.createUserKeycloak(user, 1);
        } else {
            user= userService.updateUserKeycloak(user, userService.findUserByEmail(email).getId());
        }

        ResponseCookie accessTokenCookie = createCookie("accessToken", accessTokenString, Long.parseLong(response.get("expires_in").toString()), false);
        ResponseCookie refreshTokenCookie = createCookie("refreshToken", refreshTokenString, Long.parseLong(response.get("refresh_expires_in").toString()), false);
        ResponseCookie userIdCookie = createCookie("userId", String.valueOf(user.getId()), -1, false);
        ResponseCookie schoolIdCookie = createCookie("schoolId", String.valueOf(user.getSchool().getId()), -1, false);
        ResponseCookie roleCookie = createCookie("role", role, -1, false);

        return ResponseEntity
                .ok()
                .headers(httpHeaders -> {
                    httpHeaders.put(HttpHeaders.SET_COOKIE, List.of(
                            accessTokenCookie.toString(),
                            userIdCookie.toString(),
                            schoolIdCookie.toString(),
                            roleCookie.toString(),
                            refreshTokenCookie.toString())
                    );
                })
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Long userId) {
        log.info("AuthController: Logout user");
        realmResource.users().get(userService.findById(userId).getKeycloakUserId()).logout();
        return ResponseEntity.ok()
                .headers(headers ->
                        headers.put(HttpHeaders.SET_COOKIE, List.of(
                                deleteCookie("schoolId").toString(),
                                deleteCookie("accessToken").toString(),
                                deleteCookie("refreshToken").toString(),
                                deleteCookie("userId").toString(),
                                deleteCookie("role").toString())))
                .build();
    }
}

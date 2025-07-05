package com.project.backend.auth.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.project.backend.models.User;
import com.project.backend.models.google.GoogleCalendarCredential;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.google.GoogleCalendarCredentialService;
import com.project.backend.services.inter.google.GoogleCalendarService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/google")
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthController {

    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${frontend-url}")
    private String frontendUrl;
    private static final String REDIRECT_URI = "http://localhost:8081/api/auth/google/callback";

    private final UserService userService;
    private final GoogleCalendarCredentialService googleCalendarCredentialService;
    private final GoogleCalendarService googleCalendarService;
    private final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    @GetMapping("/auth")
    public void auth(Authentication authentication, HttpServletResponse response) throws IOException, GeneralSecurityException {
        GoogleAuthorizationCodeRequestUrl url = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                jsonFactory,
                clientId,
                clientSecret,
                CalendarScopes.all()
        ).setAccessType("offline").build().newAuthorizationUrl().setRedirectUri(REDIRECT_URI).setState(userService.findUserByAuth(authentication).getId() + "");

        response.sendRedirect(url.build());
    }

    @GetMapping("/revoke")
    public void revokeAccess(Authentication authentication) {
        User user = userService.findUserByAuth(authentication);
        googleCalendarService.deleteCalendar(user.getId());
        googleCalendarCredentialService.revokeAccess(user.getId()).block();
        user.setGoogleCalendarCredential(null);
        userService.save(user);
        googleCalendarCredentialService.deleteByUser(user.getId());
    }

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, @RequestParam("state") Long userId, HttpServletResponse response) throws IOException {
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                jsonFactory,
                "https://oauth2.googleapis.com/token",
                clientId,
                clientSecret,
                code,
                REDIRECT_URI
        ).execute();

        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();

        log.info(tokenResponse.toPrettyString());
        if(googleCalendarCredentialService.existsByUserId(userId)) {
            GoogleCalendarCredential googleCalendarCredential = new GoogleCalendarCredential();
            googleCalendarCredential.setAccessToken(accessToken);
            googleCalendarCredential.setExpiresAt(LocalDateTime.now().plusSeconds(tokenResponse.getExpiresInSeconds()));
            googleCalendarCredentialService.update(googleCalendarCredential, userId);
        } else {
            GoogleCalendarCredential googleCalendarCredential = new GoogleCalendarCredential();
            googleCalendarCredential.setAccessToken(accessToken);
            googleCalendarCredential.setRefreshToken(refreshToken);
            googleCalendarCredential.setUser(userService.findById(userId));
            googleCalendarCredential.setExpiresAt(LocalDateTime.now().plusSeconds(tokenResponse.getExpiresInSeconds()));
            googleCalendarCredentialService.create(googleCalendarCredential);
            googleCalendarService.firstUploadToUserCalendar(userId);
        }

        response.sendRedirect(frontendUrl + "/profile");
    }
}

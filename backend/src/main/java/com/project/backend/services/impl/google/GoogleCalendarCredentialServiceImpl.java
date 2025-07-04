package com.project.backend.services.impl.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.project.backend.models.google.GoogleCalendarCredential;
import com.project.backend.repositories.repos.google.GoogleCalendarCredentialRepository;
import com.project.backend.services.inter.google.GoogleCalendarCredentialService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalendarCredentialServiceImpl implements GoogleCalendarCredentialService {
    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    private final GoogleCalendarCredentialRepository googleCalendarCredentialRepository;

    @Override
    public GoogleCalendarCredential findByUserId(long userId) {
        log.info("Service: get google user with id {}", userId);
        return googleCalendarCredentialRepository.findByUser_Id(userId).orElseThrow(() -> new EntityNotFoundException("Google calendar credentials for user with id " + userId + " not found"));
    }

    @Override
    public boolean existsByUserId(long userId) {
        log.info("Service: check if existing user with id {}", userId);
        return googleCalendarCredentialRepository.existsByUser_Id(userId);
    }

    @Override
    public GoogleCalendarCredential create(GoogleCalendarCredential googleCalendarCredential) {
        log.info("Service: create google calendar credential");
        return googleCalendarCredentialRepository.save(googleCalendarCredential);
    }

    @Override
    public GoogleCalendarCredential update(GoogleCalendarCredential googleCalendarCredential, long userId) {
        log.info("Service: update google calendar credential");
        GoogleCalendarCredential googleCalendarCredentialToUpdate = findByUserId(userId);
        googleCalendarCredentialToUpdate.setAccessToken(googleCalendarCredential.getAccessToken());
        return googleCalendarCredentialRepository.save(googleCalendarCredentialToUpdate);
    }

    @Override
    public GoogleCalendarCredential refresh(long userId) {
        log.info("Service: refresh google calendar credential");
        GoogleCalendarCredential googleCalendarCredential = findByUserId(userId);
        if(LocalDateTime.now().isBefore(googleCalendarCredential.getExpiresAt())) {
            return googleCalendarCredential;
        }

        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    googleCalendarCredential.getRefreshToken(),
                    clientId,
                    clientSecret
            ).execute();

            googleCalendarCredential.setAccessToken(response.getAccessToken());

            if (response.getRefreshToken() != null) {
                googleCalendarCredential.setRefreshToken(response.getRefreshToken());
            }

            googleCalendarCredential.setExpiresAt(LocalDateTime.now().plusSeconds(response.getExpiresInSeconds()));

            return googleCalendarCredentialRepository.save(googleCalendarCredential);
        } catch (IOException e) {
            throw new RuntimeException("Failed to refresh token for user " + userId, e);
        }
    }
}

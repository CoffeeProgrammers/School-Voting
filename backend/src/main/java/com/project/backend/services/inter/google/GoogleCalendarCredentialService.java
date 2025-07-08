package com.project.backend.services.inter.google;

import com.project.backend.models.google.GoogleCalendarCredential;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface GoogleCalendarCredentialService {
    Mono<Void> revokeAccess(long userId);

    GoogleCalendarCredential findByUserId(long userId);
    boolean existsByUserId(long userId);
    boolean existsByEmail(String email);
    GoogleCalendarCredential findByEmail(String email);
    GoogleCalendarCredential create(GoogleCalendarCredential googleCalendarCredential);
    GoogleCalendarCredential update(GoogleCalendarCredential googleCalendarCredential, long userId);
    GoogleCalendarCredential refresh(long userId);

    @Transactional
    void deleteByUser(long userId);
}

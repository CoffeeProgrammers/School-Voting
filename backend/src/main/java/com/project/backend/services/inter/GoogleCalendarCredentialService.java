package com.project.backend.services.inter;

import com.project.backend.models.GoogleCalendarCredential;

public interface GoogleCalendarCredentialService {
    GoogleCalendarCredential findByUserId(long userId);
    boolean existsByUserId(long userId);
    GoogleCalendarCredential create(GoogleCalendarCredential googleCalendarCredential);
    GoogleCalendarCredential update(GoogleCalendarCredential googleCalendarCredential, long userId);
    GoogleCalendarCredential refresh(long userId);
}

package com.project.backend.services.inter.google;

import com.project.backend.models.google.UserCalendar;

public interface UserCalendarService {
    UserCalendar findByUser(long userId);
    boolean existsByUser(long userId);
}

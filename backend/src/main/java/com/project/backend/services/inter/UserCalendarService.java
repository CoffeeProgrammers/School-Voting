package com.project.backend.services.inter;

import com.project.backend.models.UserCalendar;

public interface UserCalendarService {
    UserCalendar findByUser(long userId);
    boolean existsByUser(long userId);
}

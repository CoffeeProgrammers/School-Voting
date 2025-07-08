package com.project.backend.services.inter.google;

import com.project.backend.models.google.UserCalendar;

public interface UserCalendarService {
    UserCalendar create(UserCalendar userCalendar);

    UserCalendar findByUser(long userId);
    boolean existsByUser(long userId);

    void deleteByUser(long userId);
}

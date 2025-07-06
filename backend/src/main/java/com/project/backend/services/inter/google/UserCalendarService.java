package com.project.backend.services.inter.google;

import com.project.backend.models.google.UserCalendar;
import org.springframework.transaction.annotation.Transactional;

public interface UserCalendarService {
    UserCalendar create(UserCalendar userCalendar);

    UserCalendar findByUser(long userId);
    boolean existsByUser(long userId);

    @Transactional
    void deleteByUser(long userId);
}

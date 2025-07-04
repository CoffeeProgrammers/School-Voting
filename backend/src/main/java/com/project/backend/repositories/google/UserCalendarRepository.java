package com.project.backend.repositories.google;

import com.project.backend.models.google.UserCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {
    Optional<UserCalendar> findByUser_Id(long userId);
    boolean existsByUser_Id(long userId);
}

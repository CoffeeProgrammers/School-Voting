package com.project.backend.services.impl;

import com.project.backend.models.UserCalendar;
import com.project.backend.repositories.UserCalendarRepository;
import com.project.backend.services.inter.UserCalendarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCalendarServiceImpl implements UserCalendarService {
    private final UserCalendarRepository userCalendarRepository;
    @Override
    public UserCalendar findByUser(long userId) {
        return userCalendarRepository.findByUser_Id(userId).orElseThrow(()-> new EntityNotFoundException("UserCalendar with userId " + userId + " not found"));
    }

    @Override
    public boolean existsByUser(long userId) {
        return userCalendarRepository.existsByUser_Id(userId);
    }
}

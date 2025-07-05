package com.project.backend.services.impl.google;

import com.project.backend.models.google.UserCalendar;
import com.project.backend.repositories.repos.google.UserCalendarRepository;
import com.project.backend.services.inter.google.UserCalendarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCalendarServiceImpl implements UserCalendarService {
    private final UserCalendarRepository userCalendarRepository;

    @Override
    public UserCalendar create(UserCalendar userCalendar){
        return userCalendarRepository.save(userCalendar);
    }

    @Override
    public UserCalendar findByUser(long userId) {
        log.info("Service: get user calendar existing user {}", userId);
        return userCalendarRepository.findByUser_Id(userId).orElseThrow(()-> new EntityNotFoundException("UserCalendar with userId " + userId + " not found"));
    }

    @Override
    public boolean existsByUser(long userId) {
        log.info("Service: check if existing user {}", userId);
        return userCalendarRepository.existsByUser_Id(userId);
    }

    @Transactional
    @Override
    public void deleteByUser(long userId){
        log.info("Service: delete user calendar with user id {}", userId);
        userCalendarRepository.deleteById(userId);
    }
}

package com.project.backend.services.impl;

import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.services.inter.SchoolService;
import com.project.backend.services.inter.SchoolWithDirectorService;
import com.project.backend.services.inter.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolWithDirectorServiceImpl implements SchoolWithDirectorService {

    private final SchoolService schoolService;
    private final UserService userService;

    @Transactional
    @Override
    public School createSchoolWithDirector(School school, User director, String password) {
        log.info("Creating school with director {}", director.getEmail());
        userService.checkEmail(director.getEmail());
        School savedSchool = schoolService.save(school);

        director.setRole("DIRECTOR");
        director.setSchool(savedSchool);
        User savedDirector = userService.createDirector(director, password);

        savedSchool.setDirector(savedDirector);
        School finalSchool = schoolService.save(savedSchool);

        return finalSchool;
    }
}

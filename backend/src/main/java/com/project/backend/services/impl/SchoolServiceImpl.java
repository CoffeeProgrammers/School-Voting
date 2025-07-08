package com.project.backend.services.impl;

import com.project.backend.models.School;
import com.project.backend.repositories.repos.SchoolRepository;
import com.project.backend.services.inter.SchoolService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
    public School findById(long schoolId) {
        log.info("Service: School find by id {}", schoolId);
        return schoolRepository.findById(schoolId).orElseThrow(
                () -> new EntityNotFoundException("School not found with id " + schoolId));
    }

    @Override
    public School save(School school) {
        log.info("Service: School save {}", school);
        return schoolRepository.save(school);
    }

    @Override
    public School update(School school, long schoolId) {
        log.info("Service: School update {}", school);
        School schoolToUpdate = findById(schoolId);
        schoolToUpdate.setName(school.getName());
        return schoolRepository.save(schoolToUpdate);
    }

    @Override
    public void delete(long schoolId){
       log.info("Service: School delete by id {}", schoolId);
       findById(schoolId);
       schoolRepository.deleteById(schoolId);
    }
}

package com.project.backend.services.inter;

import com.project.backend.models.School;

public interface SchoolService {
    School findById(long schoolId);

    School save(School school);

    School update(School school, long schoolId);

    void delete(long schoolId);
}

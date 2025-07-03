package com.project.backend.services.inter;

import com.project.backend.models.School;

public interface SchoolService {
    School findById(long schoolId);
}

package com.project.backend.services.inter;

import com.project.backend.models.School;
import com.project.backend.models.User;

public interface SchoolWithDirectorService {
    School createSchoolWithDirector(School school, User director, String password);
}

package com.project.backend.services.inter;

import com.project.backend.models.Class;
import org.springframework.data.domain.Page;

public interface ClassService {
    Class create(Class classRequest);
    Class update(Class classRequest);
    void delete(long id);
    Class findById(long id);
    void assignUserToClass(long classId, long userId);
    void unassignUserFromClass(long classId, long userId);
    Page<Class> findAllBySchool(long schoolId, String name, long page, long size);
}

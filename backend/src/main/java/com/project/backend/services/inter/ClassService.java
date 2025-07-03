package com.project.backend.services.inter;

import com.project.backend.models.Class;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassService {
    Class create(Class classRequest, List<Long> userIds, long schoolId);

    Class update(Class classRequest, long id);
    void delete(long id);
    Class findById(long id);
    void assignUserToClass(long classId, List<Long> userIds);
    void unassignUserFromClass(long classId, List<Long> userIds);
    Page<Class> findAllBySchool(long schoolId, String name, int page, int size);
}

package com.project.backend.services.inter;

import com.project.backend.models.Class;
import com.project.backend.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassService {
    Class create(Class classRequest, List<Long> userIds, long schoolId);

    Class update(Class classRequest, long id);

    void deleteBySchool(long schoolId);

    void delete(long id, boolean deleteUsers);
    Class findById(long id);
    List<Long> assignUserToClass(long classId, List<Long> userIds);
    void unassignUserFromClass(long classId, List<Long> userIds);
    Page<Class> findAllBySchool(long schoolId, String name, int page, int size);
    List<Class> findAllBySchool(long schoolId);

    Class findByUser(User user);
}

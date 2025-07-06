package com.project.backend.services.inter;

import com.project.backend.models.User;

public interface UserDeletionService {
    void delete(User user, boolean isDeleteDirector);

    void deleteAllBySchool(long schoolId);
}

package com.project.backend.repositories.specification;

import com.project.backend.models.Class;
import com.project.backend.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class ClassSpecification {
    public static Specification<Class> bySchool(long schoolId) {
        log.info("Building specification: bySchool(schoolId = {})", schoolId);
        return (root, criteriaQuery, cb) ->
                cb.equal(root.get("school").get("id"), schoolId);
    }

    public static Specification<Class> byName(String name) {
        log.info("Building specification: byName(name = '{}')", name);
        return (root, criteriaQuery, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Class> hasUser(User user) {
        log.info("Building specification: hasUser(userId = {})", user.getId());
        return (root, criteriaQuery, cb) ->
                cb.isMember(user, root.get("users"));
    }
}

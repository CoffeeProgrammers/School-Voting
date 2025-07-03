package com.project.backend.repositories.specification;

import com.project.backend.models.Class;
import org.springframework.data.jpa.domain.Specification;

public class ClassSpecification {
    public static Specification<Class> bySchool(long schoolId) {
        return (root, criteriaQuery, cb) ->
                cb.equal(root.get("school").get("id"), schoolId);
    }
    public static Specification<Class> byName(String name) {
        return (root, criteriaQuery, cb) ->
                cb.like(root.get("name"), name);
    }
}

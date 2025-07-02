package com.project.backend.repositories.specification;

import com.project.backend.models.User;
import com.project.backend.models.petitions.Petition;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class PetitionSpecification {
    public static Specification<Petition> bySchool(long schoolId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("school").get("id"), schoolId);
    }
    public static Specification<Petition> byUserInSchool(long userId) {
        return (root, query, cb) -> {
            Join<Object, Object> classJoin = root.join("school").join("classes");
            Join<Object, User> userJoin = classJoin.join("users");
            return cb.equal(userJoin.get("id"), userId);
        };
    }
    public static Specification<Petition> byUserInClass(long userId) {
        return (root, query, cb) -> {
            Join<Object, Object> classJoin = root.join("myClass");
            Join<Object, User> userJoin = classJoin.join("users");
            return cb.equal(userJoin.get("id"), userId);
        };
    }
    public static Specification<Petition> byCreator(long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("creator").get("id"), userId);
    }
    public static Specification<Petition> byStatus(String status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }
    public static Specification<Petition> byName(String name) {
        return (root, query, cb) ->
                cb.like(root.get("name"), name);
    }
}

package com.project.backend.repositories.specification;

import com.project.backend.models.User;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification {

    public static Specification<User> byRole(String role) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), role));
    }

    public static Specification<User> byFirstName(String firstName) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("firstName"), firstName));
    }

    public static Specification<User> byLastName(String lastName) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("lastName"), lastName));
    }

    public static Specification<User> byEmail(String email) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), email));
    }

    public static Specification<User> byClass(long classId){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("class_id"), classId));
    }

    public static Specification<User> bySchool(long schoolId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("school_id"), schoolId));
    }

    public static Specification<User> notUser(Long userId) {
        return (root, query, cb) ->
                cb.notEqual(root.get("id"), userId);
    }

    public static Specification<User> notIncludeDeleted(){
        String emailOfDeletedUser = "!deleted-user!@deleted.com";
        return (root, query, cb) ->
                cb.notEqual(root.get("email"), emailOfDeletedUser);
    }
}

package com.project.backend.repositories.specification;

import com.project.backend.models.User;
import com.project.backend.models.VotingUser;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
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
                criteriaBuilder.equal(root.get("myClass").get("id"), classId));
    }

    public static Specification<User> notInAnyClass() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("myClass").get("id")));
    }

    public static Specification<User> bySchool(long schoolId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("school").get("id"), schoolId));
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

    public static Specification<User> usersByVotingSortedByAnswer(Long votingId) {
        return (root, query, cb) -> {
            Join<User, VotingUser> votingUserJoin = root.join("votingUsers", JoinType.LEFT);

            Predicate votingFilter = cb.equal(votingUserJoin.get("voting").get("id"), votingId);

            query.orderBy(
                    cb.asc(cb.isNull(votingUserJoin.get("answer"))),
                    cb.asc(root.get("lastName")),
                    cb.asc(root.get("firstName"))
            );

            return votingFilter;
        };
    }

}

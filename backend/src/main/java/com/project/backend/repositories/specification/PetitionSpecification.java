package com.project.backend.repositories.specification;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
import com.project.backend.models.petition.Petition;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PetitionSpecification {
    public static Specification<Petition> bySchool(long schoolId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("targetId"), schoolId));
            predicates.add(cb.equal(root.get("levelType"), LevelType.SCHOOL));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<Petition> byClass(long classId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("targetId"), classId));
            predicates.add(cb.equal(root.get("levelType"), LevelType.CLASS));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<Petition> byUserInSchool(User user) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("targetId"), user.getSchool().getId()),
                cb.equal(root.get("levelType"), LevelType.SCHOOL)
        );
    }

    public static Specification<Petition> byUserInClass(User user) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("targetId"), user.getMyClass().getId()),
                cb.equal(root.get("levelType"), LevelType.CLASS)
        );
    }

    public static Specification<Petition> byCreator(long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("creator").get("id"), userId);
    }

    public static Specification<Petition> byStatus(Status status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Petition> byName(String name) {
        return (root, query, cb) ->
                cb.like(root.get("name"), name);
    }

    public static Specification<Petition> byUser(User user) {
        return (root, query, cb) -> cb.or(
                cb.and(
                        cb.equal(root.get("levelType"), LevelType.SCHOOL),
                        cb.equal(root.get("creator").get("school").get("id"), user.getSchool().getId())
                ),
                cb.and(
                        cb.equal(root.get("levelType"), LevelType.CLASS),
                        cb.equal(root.get("targetId"), user.getMyClass().getId())
                )
        );
    }

    public static Specification<Petition> byUserWithVote(User user) {
        return (root, query, cb) ->
                cb.isMember(user, root.get("users"));
    }


    public static Specification<Petition> expired() {
        return (root, query, cb) -> cb.lessThan(root.get("endTime"), LocalDateTime.now());
    }
}

package com.project.backend.repositories.specification;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
import com.project.backend.models.petition.Petition;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PetitionSpecification {
    public static Specification<Petition> bySchoolForDirector(long schoolId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("creator").get("school").get("id"), schoolId));
    }
    public static Specification<Petition> bySchool(long schoolId) {
        log.info("Building specification: bySchool(schoolId = {})", schoolId);
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("targetId"), schoolId));
            predicates.add(cb.equal(root.get("levelType"), LevelType.SCHOOL));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Petition> byClass(long classId) {
        log.info("Building specification: byClass(classId = {})", classId);
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("targetId"), classId));
            predicates.add(cb.equal(root.get("levelType"), LevelType.CLASS));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Petition> byUserInSchool(User user) {
        long schoolId = user.getSchool().getId();
        log.info("Building specification: byUserInSchool(schoolId = {})", schoolId);
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("targetId"), schoolId),
                cb.equal(root.get("levelType"), LevelType.SCHOOL)
        );
    }

    public static Specification<Petition> byUserInClass(User user) {
        long classId = user.getMyClass().getId();
        log.info("Building specification: byUserInClass(classId = {})", classId);
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("targetId"), classId),
                cb.equal(root.get("levelType"), LevelType.CLASS)
        );
    }

    public static Specification<Petition> byCreator(long userId) {
        log.info("Building specification: byCreator(userId = {})", userId);
        return (root, query, cb) ->
                cb.equal(root.get("creator").get("id"), userId);
    }

    public static Specification<Petition> byStatus(Status status) {
        log.info("Building specification: byStatus(status = {})", status);
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Petition> byName(String name) {
        log.info("Building specification: byName(name = '{}')", name);
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Petition> byUser(User user) {
        long schoolId = user.getSchool().getId();
        long classId = user.getMyClass().getId();
        log.info("Building specification: byUser(schoolId = {}, classId = {})", schoolId, classId);
        return (root, query, cb) -> cb.or(
                cb.and(
                        cb.equal(root.get("levelType"), LevelType.SCHOOL),
                        cb.equal(root.get("creator").get("school").get("id"), schoolId)
                ),
                cb.and(
                        cb.equal(root.get("levelType"), LevelType.CLASS),
                        cb.equal(root.get("targetId"), classId)
                )
        );
    }

    public static Specification<Petition> byUserWithVote(User user) {
        log.info("Building specification: byUserWithVote(userId = {})", user.getId());
        return (root, query, cb) ->
                cb.isMember(user, root.get("users"));
    }

    public static Specification<Petition> expired() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Building specification: expired(before = {})", now);
        return (root, query, cb) -> cb.lessThan(root.get("endTime"), now);
    }
}

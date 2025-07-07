package com.project.backend.repositories.specification;

import com.project.backend.models.enums.LevelType;
import com.project.backend.models.voting.Voting;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class VotingSpecification {

    private static final Logger log = LoggerFactory.getLogger(VotingSpecification.class);

    public static Specification<Voting> byCreator(long creatorId) {
        log.info("Building specification: byCreator(creatorId = {})", creatorId);
        return (root, query, cb) ->
                cb.equal(root.get("creator").get("id"), creatorId);
    }

    public static Specification<Voting> byDirector(long directorId) {
        log.info("Building specification: byDirector(directorId = {})", directorId);
        return (root, query, cb) ->
                cb.equal(root.get("creator").get("school").get("director").get("id"), directorId);
    }

    public static Specification<Voting> byUser(long userId) {
        log.info("Building specification: byUser(userId = {})", userId);
        return (root, query, cb) -> {
            Predicate predicate = cb.equal(root.join("votingUsers").get("user").get("id"), userId);
            Objects.requireNonNull(query).distinct(true);
            return predicate;
        };
    }

    public static Specification<Voting> byUserInClass(long userId) {
        log.info("Building specification: byUserInClass(userId = {})", userId);
        return (root, query, cb) -> {
            Predicate predicate = cb.and(
                    cb.equal(root.join("votingUsers").get("user").get("id"), userId),
                    cb.equal(root.get("levelType"), LevelType.CLASS));
            Objects.requireNonNull(query).distinct(true);
            return predicate;
        };
    }

    public static Specification<Voting> byName(String name) {
        log.info("Building specification: byName(name = '{}')", name);
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Voting> byStartTimeAndEndTime() {
        LocalDate today = LocalDate.now();
        log.info("Building specification: byStartTimeAndEndTime(today = {})", today);
        return (root, query, cb) -> {
            Predicate start = cb.lessThanOrEqualTo(root.get("startTime"), today);
            Predicate end = cb.greaterThanOrEqualTo(root.get("endTime"), today);
            return cb.and(start, end);
        };
    }

    public static Specification<Voting> byStartTime() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Building specification: byStartTime(now = {})", now);
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("startTime"), now);
    }

    public static Specification<Voting> byStartTimeNot() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Building specification: byStartTimeNot(now = {})", now);
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("startTime"), now);
    }

    public static Specification<Voting> ended() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Building specification: ended(after = {})", now);
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("endTime"), now);
    }

    public static Specification<Voting> isNotVoted(long userId) {
        log.info("Building specification: isNotVoted(userId = {})", userId);
        return (root, query, cb) -> {
            Objects.requireNonNull(query).distinct(true);
            var join = root.join("votingUsers", JoinType.LEFT);
            return cb.and(
                    cb.equal(join.get("user").get("id"), userId),
                    cb.isNull(join.get("answer"))
            );
        };
    }

    public static Specification<Voting> isVoted(long userId) {
        log.info("Building specification: isVoted(userId = {})", userId);
        return (root, query, cb) -> {
            Objects.requireNonNull(query).distinct(true);
            var join = root.join("votingUsers", JoinType.LEFT);
            return cb.and(
                    cb.equal(join.get("user").get("id"), userId),
                    cb.isNotNull(join.get("answer"))
            );
        };
    }

    public static Specification<Voting> byClass(long classId) {
        log.info("Building specification: byClass(classId = {}) [TODO]", classId);
        return (root, query, cb) ->
                cb.and(cb.equal(root.get("levelType"), LevelType.CLASS),
                        cb.equal(root.get("targetId"), classId));
    }
}

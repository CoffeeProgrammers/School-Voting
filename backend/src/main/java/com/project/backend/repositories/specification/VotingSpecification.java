package com.project.backend.repositories.specification;

import com.project.backend.models.enums.LevelType;
import com.project.backend.models.voting.Voting;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VotingSpecification {
    public static Specification<Voting> byCreator(long creatorId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("creator").get("id"), creatorId);
    }

    public static Specification<Voting> byDirector(long directorId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("creator").get("school").get("director").get("id"), directorId);
    }

    public static Specification<Voting> byUser(long userId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("votingUsers").get("user").get("id"), userId));
    }

    public static Specification<Voting> byUserInClass(long userId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.join("votingUsers").get("user").get("id"), userId),
                        criteriaBuilder.equal(root.get("levelType"), LevelType.CLASS)));
    }

    public static Specification<Voting> byName(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), name));
    }

    public static Specification<Voting> byStartTimeAndEndTime() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            Predicate startDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), today);
            Predicate endDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), today);
            return criteriaBuilder.and(startDatePredicate, endDatePredicate);
        };
    }

    public static Specification<Voting> byStartTime() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime now = LocalDateTime.now();
            Predicate startTimePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), now);
            return criteriaBuilder.and(startTimePredicate);
        };
    }

    public static Specification<Voting> byStartTimeNot() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime now = LocalDateTime.now();
            Predicate startTimePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), now);
            return criteriaBuilder.and(startTimePredicate);
        };
    }

    public static Specification<Voting> ended() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), LocalDateTime.now()));
    }

    public static Specification<Voting> isNotVoted(long userId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.join("votingUsers").get("answer")));
    }

    public static Specification<Voting> isVoted(long userId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.join("votingUsers").get("answer")));
    }

    public static Specification<Voting> byClass(long classId) {
        return ((root, query, criteriaBuilder) ->
                null); //TODO
    }
}

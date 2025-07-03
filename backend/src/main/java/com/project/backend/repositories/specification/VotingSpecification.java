package com.project.backend.repositories.specification;

import com.project.backend.models.voting.Voting;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public static Specification<Voting> byName(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), name));
    }

    public static Specification<Voting> byStartDateAndEndDate() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            Predicate startDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), today);
            Predicate endDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), today);
            return criteriaBuilder.and(startDatePredicate, endDatePredicate);
        };
    }

    public static Specification<Voting> byStartDate() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            Predicate startDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), today);
            return criteriaBuilder.and(startDatePredicate);
        };
    }

    public static Specification<Voting> ended() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("endDate"), LocalDate.now()));
    }

    public static Specification<Voting> canVote(long userId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.join("votingUsers").get("answer")));
    }

    public static Specification<Voting> cantVote(long userId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.join("votingUsers").get("answer")));
    }
}

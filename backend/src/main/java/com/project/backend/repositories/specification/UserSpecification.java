package com.project.backend.repositories.specification;

import com.project.backend.models.User;
import com.project.backend.models.voting.Voting;
import com.project.backend.models.voting.VotingUser;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class UserSpecification {

    private static final Logger log = LoggerFactory.getLogger(UserSpecification.class);

    public static Specification<User> byRole(String role) {
        log.info("Building specification: byRole(role = {})", role);
        return (root, query, cb) -> cb.equal(root.get("role"), role);
    }

    public static Specification<User> byFirstName(String firstName) {
        log.info("Building specification: byFirstName(firstName = '{}')", firstName);
        return (root, query, cb) -> cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> byLastName(String lastName) {
        log.info("Building specification: byLastName(lastName = '{}')", lastName);
        return (root, query, cb) -> cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> byEmail(String email) {
        log.info("Building specification: byEmail(email = '{}')", email);
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> byClass(long classId) {
        log.info("Building specification: byClass(classId = {})", classId);
        return (root, query, cb) -> cb.equal(root.get("myClass").get("id"), classId);
    }

    public static Specification<User> notInAnyClass() {
        log.info("Building specification: notInAnyClass()");
        return (root, query, cb) -> cb.isNull(root.get("myClass").get("id"));
    }

    public static Specification<User> bySchool(long schoolId) {
        log.info("Building specification: bySchool(schoolId = {})", schoolId);
        return (root, query, cb) -> cb.equal(root.get("school").get("id"), schoolId);
    }

    public static Specification<User> notUser(Long userId) {
        log.info("Building specification: notUser(userId = {})", userId);
        return (root, query, cb) -> cb.notEqual(root.get("id"), userId);
    }

//    public static Specification<User> notIncludeDeleted() {
//        String emailOfDeletedUser = "!deleted-user!@deleted.com";
//        log.info("Building specification: notIncludeDeleted(email != '{}')", emailOfDeletedUser);
//        return (root, query, cb) -> cb.notEqual(root.get("email"), emailOfDeletedUser);
//    }

    public static Specification<User> usersByVotingSortedByAnswer(Long votingId) {
        log.info("Building specification: usersByVotingSortedByAnswer(votingId = {})", votingId);
        return (root, query, cb) -> {
            Join<User, VotingUser> votingUserJoin = root.join("votingUsers", JoinType.LEFT);

            Predicate votingFilter = cb.equal(votingUserJoin.get("voting").get("id"), votingId);

            Objects.requireNonNull(query).distinct(true);
            query.orderBy(
                    cb.asc(cb.isNull(votingUserJoin.get("answer"))),
                    cb.asc(root.get("lastName")),
                    cb.asc(root.get("firstName"))
            );

            return votingFilter;
        };
    }

    public static Specification<User> connectedToGoogle() {
        log.info("Building specification: connectedToGoogle()");
        return (root, query, cb) -> cb.isNotNull(root.get("googleCalendarCredential"));
    }

//    public static Specification<User> byPetition(Petition petition) {
//        log.info("Building specification: byPetition(petitionId = {})", petition.getId());
//        return (root, query, cb) -> cb.isMember(petition, root.get("petitions"));
//    }

    public static Specification<User> byVoting(Voting voting) {
        log.info("Building specification: byVoting(votingId = {})", voting.getId());
        return (root, query, cb) -> {
            Join<User, VotingUser> votingUserJoin = root.join("votingUsers", JoinType.LEFT);
            Predicate predicate = cb.equal(votingUserJoin.get("voting"), voting);
            Objects.requireNonNull(query).distinct(true);
            return predicate;
        };
    }
}

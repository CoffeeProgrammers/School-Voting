package com.project.backend;

import com.project.backend.models.Class;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.google.GoogleCalendarCredential;
import com.project.backend.models.google.UserCalendar;
import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.petition.Comment;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.models.voting.VotingUser;
import org.instancio.Instancio;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.instancio.Select.field;

public class TestUtil {
    public static User createUser(String role, String email) {
        User user = Instancio.of(User.class).ignore(field("role")).ignore(field("email")).create();
        user.setRole(role);
        user.setEmail(email);
        return user;
    }

    public static User createUser(String role, String email, String keycloakUserId) {
        User user = createUser(role, email);
        user.setKeycloakUserId(keycloakUserId);
        return user;
    }

    public static School createSchool(String name) {
        School school = Instancio.of(School.class).ignore(field("name")).create();
        school.setName(name);
        return school;
    }

    public static School createSchool(String name, User director) {
        School school = createSchool(name);
        school.setDirector(director);
        return school;
    }

    public static void assignUsersToSchool(School school, User... users) {
        Arrays.stream(users).forEach(u -> u.setSchool(school));
    }

    public static Class createClass(String name) {
        Class clazz = Instancio.of(Class.class).ignore(field("name")).create();
        clazz.setName(name);
        return clazz;
    }

    public static Class createClass(String name, List<User> students, School school) {
        Class clazz = createClass(name);
        clazz.setUsers(new HashSet<>(students));
        clazz.setSchool(school);
        return clazz;
    }

    public static Voting createVoting(String name) {
        return Instancio.of(Voting.class).ignore(field("name")).create();
    }

    public static VotingUser createVotingUser() {
        return Instancio.of(VotingUser.class).create();
    }

    public static VotingUser.VotingUserId createVotingUserId() {
        return Instancio.of(VotingUser.VotingUserId.class).create();
    }

    public static Answer createAnswer(String name) {
        Answer answer = Instancio.of(Answer.class).ignore(field("name")).create();
        answer.setName(name);
        return answer;
    }

    public static Comment createComment(String text) {
        Comment comment = Instancio.of(Comment.class).ignore(field("text")).create();
        comment.setText(text);
        return comment;
    }

    public static Petition createPetition(String name) {
        Petition petition = Instancio.of(Petition.class).ignore(field("name")).create();
        petition.setName(name);
        return petition;
    }

    public static GoogleCalendarCredential createGoogleCalendarCredential() {
        return Instancio.of(GoogleCalendarCredential.class).create();
    }

    public static UserCalendar createUserCalendar() {
        return Instancio.of(UserCalendar.class).create();
    }

    public static UserPetitionEvent createUserPetitionEvent() {
        return Instancio.of(UserPetitionEvent.class).create();
    }

    public static UserVotingEvent createUserVotingEvent() {
        return Instancio.of(UserVotingEvent.class).create();
    }

    public static UserVotingEvent.UserVotingEventId createUserVotingEventId() {
        return Instancio.of(UserVotingEvent.UserVotingEventId.class).create();
    }

    public static UserPetitionEvent.UserPetitionEventId createUserPetitionEventId() {
        return Instancio.of(UserPetitionEvent.UserPetitionEventId.class).create();
    }
}

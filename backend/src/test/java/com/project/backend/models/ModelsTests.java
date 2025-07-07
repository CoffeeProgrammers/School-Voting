package com.project.backend.models;


import com.project.backend.TestUtil;
import com.project.backend.models.google.GoogleCalendarCredential;
import com.project.backend.models.google.UserCalendar;
import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.petition.Comment;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
import com.project.backend.models.voting.VotingUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelsTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testUser() {
        User user1 = TestUtil.createUser("STUDENT", "test@gmail.com", "Some-new-ew");
        User user2 = new User();
        user2.setEmail(user1.getEmail());
        user2.setRole(user1.getRole());
        user2.setMyClass(user1.getMyClass());
        user2.setId(user1.getId());
        user2.setSchool(user1.getSchool());
        user2.setKeycloakUserId(user1.getKeycloakUserId());
        user2.setVotingUsers(user1.getVotingUsers());
        user2.setGoogleCalendarCredential(user1.getGoogleCalendarCredential());
        user2.setFirstName(user1.getFirstName());
        user2.setLastName(user1.getLastName());
        user2.setPetitions(user1.getPetitions());
        user2.setUserPetitionEvents(user1.getUserPetitionEvents());
        user2.setUserVotingEvents(user1.getUserVotingEvents());

        assertEquals(user1, user2);
        assertEquals(user1.toString(), user2.toString());
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testSchool() {
        School school1 = TestUtil.createSchool("School", TestUtil.createUser("Role", "some"));
        School school2 = new School();
        school2.setId(school1.getId());
        school2.setName(school1.getName());
        school2.setDirector(school1.getDirector());
        school2.setClasses(school1.getClasses());

        assertEquals(school1, school2);
        assertEquals(school1.toString(), school2.toString());
        assertEquals(school1.hashCode(), school2.hashCode());
    }

    @Test
    void testClass() {
        Class class1 = TestUtil.createClass("Some",
                List.of(TestUtil.createUser("Role", "some")), TestUtil.createSchool("Some"));
        Class class2 = new Class();
        class2.setId(class1.getId());
        class2.setName(class1.getName());
        class2.setUsers(class1.getUsers());
        class2.setSchool(class1.getSchool());

        assertEquals(class1, class2);
        assertEquals(class1.toString(), class2.toString());
        assertEquals(class1.hashCode(), class2.hashCode());
    }

    @Test
    void testVoting() {
        Voting voting1 = TestUtil.createVoting("New name");
        Voting voting2 = new Voting();
        voting2.setId(voting1.getId());
        voting2.setName(voting1.getName());
        voting2.setDescription(voting1.getDescription());
        voting2.setCountAll(voting1.getCountAll());
        voting2.setStartTime(voting1.getStartTime());
        voting2.setEndTime(voting1.getEndTime());
        voting2.setCreator(voting1.getCreator());
        voting2.setVotingUsers(voting1.getVotingUsers());
        voting2.setLevelType(voting1.getLevelType());
        voting2.setUserVotingEvents(voting1.getUserVotingEvents());
        voting2.setTargetId(voting1.getTargetId());

        assertEquals(voting1, voting2);
        assertEquals(voting1.toString(), voting2.toString());
        assertEquals(voting1.hashCode(), voting2.hashCode());
        assertEquals(voting1.now(), voting2.now());
    }

    @Test
    void testVotingUser() {
        VotingUser votingUser1 = TestUtil.createVotingUser();
        VotingUser votingUser2 = new VotingUser();

        VotingUser votingUser3 = new VotingUser(votingUser1.getVoting(), votingUser1.getUser());
        votingUser3.setId(votingUser1.getId());

        votingUser2.setId(votingUser1.getId());
        votingUser2.setVoting(votingUser1.getVoting());
        votingUser2.setUser(votingUser1.getUser());

        assertEquals(votingUser1, votingUser2);
        assertEquals(votingUser1.toString(), votingUser2.toString());
        assertEquals(votingUser1.hashCode(), votingUser2.hashCode());
        assertEquals(votingUser1, votingUser3);
    }

    @Test
    void testVotingUserId() {
        VotingUser.VotingUserId votingUserId1 = TestUtil.createVotingUserId();
        VotingUser.VotingUserId votingUserId2 = new VotingUser.VotingUserId();
        VotingUser.VotingUserId votingUserId3 = new VotingUser.VotingUserId(votingUserId1.getUserId(), votingUserId1.getVotingId());
        votingUserId2.setUserId(votingUserId1.getUserId());
        votingUserId2.setVotingId(votingUserId1.getVotingId());

        assertEquals(votingUserId1, votingUserId2);
        assertEquals(votingUserId1, votingUserId3);
        assertEquals(votingUserId1.toString(), votingUserId2.toString());
        assertEquals(votingUserId1.hashCode(), votingUserId2.hashCode());
    }

    @Test
    void testAnswer() {
        Answer answer1 = TestUtil.createAnswer("Some");
        Answer answer2 = new Answer();
        Answer answer3 = new Answer(answer1.getName(), answer1.getVoting());
        answer3.incrementCount();

        answer2.setId(answer1.getId());
        answer2.setVoting(answer1.getVoting());
        answer2.setCount(answer1.getCount());
        answer2.setName(answer1.getName());

        assertEquals(answer1, answer2);
        assertEquals(answer1.toString(), answer2.toString());
        assertEquals(answer1.hashCode(), answer2.hashCode());
    }

    @Test
    void testComment() {
        Comment comment1 = TestUtil.createComment("Some");
        Comment comment2 = new Comment();

        comment2.setId(comment1.getId());
        comment2.setCreator(comment1.getCreator());
        comment2.setText(comment1.getText());
        comment2.setPetition(comment1.getPetition());
        comment2.setCreatedTime(comment1.getCreatedTime());

        assertEquals(comment1, comment2);
        assertEquals(comment1.toString(), comment2.toString());
        assertEquals(comment1.hashCode(), comment2.hashCode());
    }

    @Test
    void testPetition() {
        Petition petition1 = TestUtil.createPetition("Some");
        Petition petition2 = new Petition();

        petition2.setId(petition1.getId());
        petition2.setCreator(petition1.getCreator());
        petition2.setCount(petition1.getCount());
        petition2.setCountNeeded(petition1.getCountNeeded());
        petition2.setDescription(petition1.getDescription());
        petition2.setStatus(petition1.getStatus());
        petition2.setUsers(petition1.getUsers());
        petition2.setLevelType(petition1.getLevelType());
        petition2.setTargetId(petition1.getTargetId());
        petition2.setEndTime(petition1.getEndTime());
        petition2.setCreationTime(petition1.getCreationTime());
        petition2.setTargetName(petition1.getTargetName());
        petition2.setName(petition1.getName());

        petition2.incrementCount();
        petition2.decrementCount();

        assertEquals(petition1, petition2);
        assertEquals(petition1.toString(), petition2.toString());
        assertEquals(petition1.hashCode(), petition2.hashCode());
        assertEquals(petition1.now(), petition2.now());
    }

    @Test
    void testGoogleCalendarCredential() {
        GoogleCalendarCredential googleCalendarCredential1 = TestUtil.createGoogleCalendarCredential();
        GoogleCalendarCredential googleCalendarCredential2 = new GoogleCalendarCredential();

        googleCalendarCredential2.setId(googleCalendarCredential1.getId());
        googleCalendarCredential2.setUser(googleCalendarCredential1.getUser());
        googleCalendarCredential2.setExpiresAt(googleCalendarCredential1.getExpiresAt());
        googleCalendarCredential2.setAccessToken(googleCalendarCredential1.getAccessToken());
        googleCalendarCredential2.setRefreshToken(googleCalendarCredential1.getRefreshToken());

        assertEquals(googleCalendarCredential1, googleCalendarCredential2);
        assertEquals(googleCalendarCredential1.toString(), googleCalendarCredential2.toString());
        assertEquals(googleCalendarCredential1.hashCode(), googleCalendarCredential2.hashCode());
    }

    @Test
    void testUserCalendar() {
        UserCalendar userCalendar1 = TestUtil.createUserCalendar();
        UserCalendar userCalendar2 = new UserCalendar();

        userCalendar2.setId(userCalendar1.getId());
        userCalendar2.setUser(userCalendar1.getUser());
        userCalendar2.setCalendarId(userCalendar1.getCalendarId());

        assertEquals(userCalendar1, userCalendar2);
        assertEquals(userCalendar1.toString(), userCalendar2.toString());
        assertEquals(userCalendar1.hashCode(), userCalendar2.hashCode());
    }

    @Test
    void testUserPetitionEvent() {
        UserPetitionEvent userPetitionEvent1 = TestUtil.createUserPetitionEvent();
        UserPetitionEvent userPetitionEvent2 = new UserPetitionEvent();

        userPetitionEvent2.setId(userPetitionEvent1.getId());
        userPetitionEvent2.setUser(userPetitionEvent1.getUser());
        userPetitionEvent2.setPetition(userPetitionEvent1.getPetition());
        userPetitionEvent2.setReminderEventId(userPetitionEvent1.getReminderEventId());
        userPetitionEvent2.setEventId(userPetitionEvent1.getEventId());

        assertEquals(userPetitionEvent1, userPetitionEvent2);
        assertEquals(userPetitionEvent1.toString(), userPetitionEvent2.toString());
        assertEquals(userPetitionEvent1.hashCode(), userPetitionEvent2.hashCode());
    }

    @Test
    void testUserPetitionEventId() {
        UserPetitionEvent.UserPetitionEventId userPetitionEventId1 = TestUtil.createUserPetitionEventId();
        UserPetitionEvent.UserPetitionEventId userPetitionEventId2 = new UserPetitionEvent.UserPetitionEventId();
        UserPetitionEvent.UserPetitionEventId userPetitionEventId3 =
                new UserPetitionEvent.UserPetitionEventId(userPetitionEventId1.getUserId(), userPetitionEventId1.getPetitionId());

        userPetitionEventId2.setPetitionId(userPetitionEventId1.getPetitionId());
        userPetitionEventId2.setUserId(userPetitionEventId1.getUserId());

        assertEquals(userPetitionEventId1, userPetitionEventId2);
        assertEquals(userPetitionEventId1.toString(), userPetitionEventId2.toString());
        assertEquals(userPetitionEventId1.hashCode(), userPetitionEventId2.hashCode());
    }

    @Test
    void testUserVotingEvent() {
        UserVotingEvent userVotingEvent1 = TestUtil.createUserVotingEvent();
        UserVotingEvent userVotingEvent2 = new UserVotingEvent();

        userVotingEvent2.setId(userVotingEvent1.getId());
        userVotingEvent2.setUser(userVotingEvent1.getUser());
        userVotingEvent2.setVoting(userVotingEvent1.getVoting());
        userVotingEvent2.setReminderEventId(userVotingEvent1.getReminderEventId());
        userVotingEvent2.setEventId(userVotingEvent1.getEventId());

        assertEquals(userVotingEvent1, userVotingEvent2);
        assertEquals(userVotingEvent1.toString(), userVotingEvent2.toString());
        assertEquals(userVotingEvent1.hashCode(), userVotingEvent2.hashCode());
    }

    @Test
    void testUserVotingEventId() {
        UserVotingEvent.UserVotingEventId userVotingEventId1 = TestUtil.createUserVotingEventId();
        UserVotingEvent.UserVotingEventId userVotingEventId2 = new UserVotingEvent.UserVotingEventId();
        UserVotingEvent.UserVotingEventId userVotingEventId3 = new UserVotingEvent.UserVotingEventId(userVotingEventId1.getUserId(), userVotingEventId1.getVotingId());

        userVotingEventId2.setUserId(userVotingEventId1.getUserId());
        userVotingEventId2.setVotingId(userVotingEventId1.getVotingId());

        assertEquals(userVotingEventId1, userVotingEventId2);
        assertEquals(userVotingEventId1.toString(), userVotingEventId2.toString());
        assertEquals(userVotingEventId1.hashCode(), userVotingEventId2.hashCode());
    }
}

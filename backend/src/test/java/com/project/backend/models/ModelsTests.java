package com.project.backend.models;


import com.project.backend.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelsTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testUser() {
        User user1 = TestUtil.createUser("STUDENT", "test@gmail.com");
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

    // TODO
    @Test
    void testClass() {

    }

    @Test
    void testSchool() {

    }

    @Test
    void testAnswer() {

    }

    @Test
    void testVoting() {

    }

    @Test
    void testVotingUser() {

    }

    @Test
    void testVotingUserId() {

    }

    @Test
    void testComment() {

    }

    @Test
    void testPetition() {

    }

    @Test
    void testGoogleCalendarCredential() {

    }

    @Test
    void testUserCalendar() {

    }

    @Test
    void testUserPetitionEvent() {

    }

    @Test
    void testUserVotingEvent() {

    }
}

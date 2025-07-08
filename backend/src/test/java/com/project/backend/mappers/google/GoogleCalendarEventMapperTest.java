package com.project.backend.mappers.google;

import com.google.api.services.calendar.model.Event;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleCalendarEventMapperTest {

    private static final String TIMEZONE = "Europe/Kyiv";

    @Test
    void fromVotingToEvent_withSchoolLocation() {
        Voting voting = new Voting();
        voting.setName("Voting 1");
        voting.setDescription("Vote description");
        voting.setStartTime(LocalDateTime.now());
        voting.setEndTime(LocalDateTime.now().plusDays(1));

        User creator = new User();
        School school = new School();
        school.setName("My School");
        creator.setSchool(school);
        voting.setCreator(creator);

        Event event = GoogleCalendarEventMapper.fromVotingToEvent(voting);

        assertEquals("VOTING: Voting 1", event.getSummary());
        assertEquals("Vote description", event.getDescription());
        assertEquals("My School", event.getLocation());
        assertNotNull(event.getStart());
        assertNotNull(event.getEnd());
        assertEquals(TIMEZONE, event.getStart().getTimeZone());
        assertEquals(TIMEZONE, event.getEnd().getTimeZone());
        assertNotNull(event.getReminders());
        assertFalse(event.getReminders().getUseDefault());
        assertEquals(1, event.getReminders().getOverrides().size());
        assertEquals(30, event.getReminders().getOverrides().get(0).getMinutes());
    }

    @Test
    void fromVotingToEvent_withoutSchoolLocation() {
        Voting voting = new Voting();
        voting.setName("Voting 2");
        voting.setDescription("Vote desc");
        voting.setStartTime(LocalDateTime.now());
        voting.setEndTime(LocalDateTime.now().plusDays(1));
        voting.setCreator(new User()); // no school set

        Event event = GoogleCalendarEventMapper.fromVotingToEvent(voting);

        assertNull(event.getLocation());
    }

    @Test
    void fromPetitionToReminderEvent() {
        Petition petition = new Petition();
        petition.setName("Petition Reminder");
        petition.setDescription("Reminder Desc");
        petition.setEndTime(LocalDateTime.now());

        Event event = GoogleCalendarEventMapper.fromPetitionToReminderEvent(petition);

        assertEquals("DEADLINE: PETITION: Petition Reminder", event.getSummary());
        assertEquals("Reminder Desc", event.getDescription());
        assertNotNull(event.getReminders());
        assertEquals(3, event.getReminders().getOverrides().size());
        assertEquals("popup", event.getReminders().getOverrides().get(0).getMethod());
        assertEquals(TIMEZONE, event.getStart().getTimeZone());
        assertEquals(TIMEZONE, event.getEnd().getTimeZone());
    }

    @Test
    void fromVotingToReminderEvent_variousDurations() {
        Voting voting = new Voting();
        voting.setName("Voting Reminder");
        voting.setDescription("Vote Reminder Desc");
        voting.setStartTime(LocalDateTime.now());

        voting.setEndTime(LocalDateTime.now().plusMinutes(20));

        Event event = GoogleCalendarEventMapper.fromVotingToReminderEvent(voting);

        // Duration < 30 mins -> no reminders
        assertTrue(event.getReminders().getOverrides().isEmpty());


        voting.setEndTime(LocalDateTime.now().plusMinutes(40)); // 40 mins duration

        event = GoogleCalendarEventMapper.fromVotingToReminderEvent(voting);

        // Duration >= 30 -> 15 mins reminder added
        assertTrue(event.getReminders().getOverrides().stream()
                .anyMatch(r -> r.getMinutes() == 15));

        voting.setEndTime(voting.getStartTime().plusHours(3)); // 3 hours

        event = GoogleCalendarEventMapper.fromVotingToReminderEvent(voting);

        // Duration >= 120 mins -> 60 mins reminder added
        assertTrue(event.getReminders().getOverrides().stream()
                .anyMatch(r -> r.getMinutes() == 60));

        voting.setEndTime(voting.getStartTime().plusDays(2)); // 2 days

        event = GoogleCalendarEventMapper.fromVotingToReminderEvent(voting);

        // Duration >= 24 * 60 -> 24*60 mins reminder added
        assertTrue(event.getReminders().getOverrides().stream()
                .anyMatch(r -> r.getMinutes() == 24 * 60));
    }

    @Test
    void testConstructor() {
        GoogleCalendarEventMapper googleCalendarEventMapper = new GoogleCalendarEventMapper();
        assertEquals(GoogleCalendarEventMapper.class, googleCalendarEventMapper.getClass());
    }
}

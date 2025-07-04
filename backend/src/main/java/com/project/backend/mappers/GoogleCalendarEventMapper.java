package com.project.backend.mappers;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.project.backend.models.petitions.Petition;
import com.project.backend.models.voting.Voting;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class GoogleCalendarEventMapper {
    public static Event fromPetitionToEvent(Petition petition) {
        Event event = new Event()
                .setSummary(petition.getName())
                .setDescription(petition.getDescription());

        LocalDateTime startLdt = petition.getEndTime().minusDays(45);
        LocalDateTime endLdt = petition.getEndTime();

        String timezone = "Europe/Kyiv";

        ZonedDateTime zdtStart = startLdt.atZone(ZoneId.of(timezone));
        DateTime start = new DateTime(zdtStart.toInstant().toEpochMilli());

        ZonedDateTime zdtEnd = endLdt.atZone(ZoneId.of(timezone));
        DateTime end = new DateTime(zdtEnd.toInstant().toEpochMilli());

        event.setStart(new EventDateTime().setDateTime(start).setTimeZone(timezone));
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(timezone));

        // TODO MATCH WITH CHANGES IN BACKEND BRANCH
        if (petition.getMyClass() != null) {
            event.setLocation(petition.getMyClass().getName());
        } else if (petition.getSchool() != null) {
            event.setLocation(petition.getSchool().getName());
        }

        if (petition.getCreator() != null && petition.getCreator().getEmail() != null) {
            EventAttendee attendee = new EventAttendee().setEmail(petition.getCreator().getEmail());
            event.setAttendees(List.of(attendee));
        }

        return event;
    }

    public static Event fromVotingToEvent(Voting voting) {
        Event event = new Event()
                .setSummary(voting.getName())
                .setDescription(voting.getDescription());

        LocalDateTime startLdt = voting.getStartTime();
        LocalDateTime endLdt = voting.getEndTime();

        String timezone = "Europe/Kyiv";

        ZonedDateTime zdtStart = startLdt.atZone(ZoneId.of(timezone));
        DateTime start = new DateTime(zdtStart.toInstant().toEpochMilli());

        ZonedDateTime zdtEnd = endLdt.atZone(ZoneId.of(timezone));
        DateTime end = new DateTime(zdtEnd.toInstant().toEpochMilli());

        event.setStart(new EventDateTime().setDateTime(start).setTimeZone(timezone));
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(timezone));

        if (voting.getCreator().getSchool() != null) {
            event.setLocation(voting.getCreator().getSchool().getName());
        }

        if (voting.getCreator() != null && voting.getCreator().getEmail() != null) {
            EventAttendee attendee = new EventAttendee().setEmail(voting.getCreator().getEmail());
            event.setAttendees(List.of(attendee));
        }

        return event;
    }
}

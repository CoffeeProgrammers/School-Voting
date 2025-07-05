package com.project.backend.mappers.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleCalendarEventMapper {
    public static Event fromPetitionToEvent(Petition petition) {
        Event event = new Event()
                .setSummary("PETITION: " + petition.getName())
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

        if (petition.getLevelType() == LevelType.CLASS) {
//            event.setLocation("CLASS: " + petition.getTargetName());
        } else if (petition.getLevelType() == LevelType.SCHOOL) {
//            event.setLocation("SCHOOL: " + petition.getTargetName());
        }

        List<EventReminder> reminders = new ArrayList<>();
        reminders.add(new EventReminder().setMethod("popup").setMinutes(30));

        Event.Reminders eventReminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(reminders);

        event.setReminders(eventReminders);

        return event;
    }

    public static Event fromVotingToEvent(Voting voting) {
        Event event = new Event()
                .setSummary("VOTING: " + voting.getName())
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

        List<EventReminder> reminders = new ArrayList<>();
        reminders.add(new EventReminder().setMethod("popup").setMinutes(30));

        Event.Reminders eventReminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(reminders);

        event.setReminders(eventReminders);
        return event;
    }

    public static Event fromPetitionToReminderEvent(Petition petition) {
        Event event = new Event()
                .setSummary("DEADLINE: PETITION: " + petition.getName())
                .setDescription(petition.getDescription());

        LocalDateTime startLdt = petition.getEndTime();
        LocalDateTime endLdt = petition.getEndTime().plusSeconds(1);

        String timezone = "Europe/Kyiv";

        ZonedDateTime zdtStart = startLdt.atZone(ZoneId.of(timezone));
        DateTime start = new DateTime(zdtStart.toInstant().toEpochMilli());

        ZonedDateTime zdtEnd = endLdt.atZone(ZoneId.of(timezone));
        DateTime end = new DateTime(zdtEnd.toInstant().toEpochMilli());

        event.setStart(new EventDateTime().setDateTime(start).setTimeZone(timezone));
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(timezone));

        EventReminder[] reminders = new EventReminder[] {
                new EventReminder().setMethod("popup").setMinutes(3 * 24 * 60),
                new EventReminder().setMethod("popup").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(60)
        };

        Event.Reminders eventReminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminders));

        event.setReminders(eventReminders);
        return event;
    }

    public static Event fromVotingToReminderEvent(Voting voting){
        Event event = new Event()
                .setSummary("DEADLINE: VOTING: " + voting.getName())
                .setDescription(voting.getDescription());

        LocalDateTime startLdt = voting.getEndTime();
        LocalDateTime endLdt = voting.getEndTime().plusSeconds(1);

        Long durationInMinutes = Duration.between(voting.getStartTime(), voting.getEndTime()).toMinutes();

        List<EventReminder> reminders = new ArrayList<>();
        if(durationInMinutes >= 30) {
           reminders.add(new EventReminder().setMethod("popup").setMinutes(15));
        }
        if(durationInMinutes >= 120) {
            reminders.add(new EventReminder().setMethod("popup").setMinutes(60));
        }
        if(durationInMinutes >= 24 * 60) {
            reminders.add(new EventReminder().setMethod("popup").setMinutes(24 * 60));
        }

        Event.Reminders eventReminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(reminders);

        event.setReminders(eventReminders);

        String timezone = "Europe/Kyiv";

        ZonedDateTime zdtStart = startLdt.atZone(ZoneId.of(timezone));
        DateTime start = new DateTime(zdtStart.toInstant().toEpochMilli());

        ZonedDateTime zdtEnd = endLdt.atZone(ZoneId.of(timezone));
        DateTime end = new DateTime(zdtEnd.toInstant().toEpochMilli());

        event.setStart(new EventDateTime().setDateTime(start).setTimeZone(timezone));
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(timezone));

        return event;
    }
}

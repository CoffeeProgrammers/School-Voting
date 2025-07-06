package com.project.backend.services.inter.google;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.project.backend.models.User;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;

public interface GoogleCalendarService {
    com.google.api.services.calendar.model.Calendar createCalendar(Calendar service, String name, String timeZone, long userId);

    void firstUploadToUserCalendar(long userId);

    void firstUploadPetitionsToUserCalendar(long userId);

    void firstUploadVotingToUserCalendar(long userId);

    Event[] savePetitionToUserCalendar(Event petitionEvent, Event petitionReminderEvent, long userId);

    Event[] saveVotingToUserCalendar(Event votingEvent, Event votingReminderEvent, long userId);

    void deletePetitionFromUserCalendar(long petitionId, long userId);
    void deleteVotingFromUserCalendar(long votingId, long userId);
    Event[] updatePetitionInUserCalendar(Event petitionEvent, Event petitionReminderEvent, long petitionId, long userId);

    Event[] updateVotingInUserCalendar(Event votingEvent, Event votingReminderEvent, long votingId, long userId);

    void deleteCalendarAndRevoke(User user);

    void deletePetitionFromUserCalendar(long petitionId);

    void deleteVotingFromUserCalendar(long votingId);

    void deleteCalendar(long userId);

    void savePetitionToUserCalendar(Petition petition);

    void saveVotingToUserCalendar(Voting voting);

    void updatePetitionToUserCalendar(Petition petition);

    void updateVotingToUserCalendar(Voting voting);
}

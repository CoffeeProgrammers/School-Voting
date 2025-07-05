package com.project.backend.services.inter.google;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;

public interface GoogleCalendarService {
    com.google.api.services.calendar.model.Calendar createCalendar(Calendar service, String name, String timeZone, long userId);

    void firstUploadToUserCalendar(long userId);

    void firstUploadPetitionsToUserCalendar(long userId);

    void firstUploadVotingToUserCalendar(long userId);

    Event[] savePetitionToUserCalendar(Petition petition, long userId);

    Event[] saveVotingToUserCalendar(Voting voting, long userId);

    void deletePetitionFromUserCalendar(long petitionId, long userId);
    void deleteVotingFromUserCalendar(long votingId, long userId);
    Event[] updatePetitionInUserCalendar(Petition petition, long userId);
    Event[] updateVotingInUserCalendar(Voting voting, long userId);

    void deleteCalendar(long userId);

    Event[] savePetitionToUserCalendar(Petition petition);

    Event[] saveVotingToUserCalendar(Voting voting);

    Event[] updatePetitionToUserCalendar(Petition petition);

    Event[] updateVotingToUserCalendar(Voting voting);
}

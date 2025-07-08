package com.project.backend.services.impl.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.project.backend.mappers.google.GoogleCalendarEventMapper;
import com.project.backend.models.User;
import com.project.backend.models.google.GoogleCalendarCredential;
import com.project.backend.models.google.UserCalendar;
import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.google.*;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {
    private final UserService userService;
    private final UserCalendarService userCalendarService;
    private final UserPetitionEventService userPetitionEventService;
    private final UserVotingEventService userVotingEventService;
    private final GoogleCalendarCredentialService googleCalendarCredentialService;
    private final PetitionService petitionService;
    private final VotingService votingService;

    @Override
    public com.google.api.services.calendar.model.Calendar createCalendar(Calendar service, String name, String timeZone, long userId) {
        log.info("Service: Creating new Google Calendar for user {}", userId);
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary(name);
        calendar.setSummary(name);
        calendar.setTimeZone(timeZone);

        com.google.api.services.calendar.model.Calendar created = null;
        try {
            created = service.calendars().insert(calendar).execute();
            log.info("Service: Created calendar with ID {} for user {}", created.getId(), userId);
        } catch (IOException e) {
            log.error("Service: Failed to create calendar for user {}", userId, e);
            throw new RuntimeException(e);
        }
        UserCalendar userCalendar = new UserCalendar();
        userCalendar.setCalendarId(created.getId());
        userCalendar.setUser(userService.findById(userId));
        userCalendarService.create(userCalendar);
        return created;
    }

    @Override
    public void firstUploadToUserCalendar(long userId) {
        log.info("Service: Starting first upload to user calendar for user {}", userId);
        Calendar service = getCalendarService(userId);
        createCalendar(service, "Coffee Programmers Voting/Petitions Calendar", "Europe/Kyiv", userId);
        firstUploadPetitionsToUserCalendar(userId);
        firstUploadVotingToUserCalendar(userId);
        log.info("Service: Finished first upload to user calendar for user {}", userId);
    }

    @Override
    public void firstUploadPetitionsToUserCalendar(long userId) {
        log.info("Service: Starting first upload petitions to user calendar for user {}", userId);
        List<Petition> myPetitions = petitionService.findAllMy(userId);
        Event[] result;
        User user = userService.findById(userId);
        Event petitionEvent;
        Event reminderPetitionEvent;
        for(Petition p : myPetitions) {
            petitionEvent = GoogleCalendarEventMapper.fromPetitionToEvent(p);
            reminderPetitionEvent = GoogleCalendarEventMapper.fromPetitionToReminderEvent(p);
            result = savePetitionToUserCalendar(petitionEvent, reminderPetitionEvent, userId);
            userPetitionEventService.create(user, p, result[0].getId(), result[1].getId());
        }
        log.info("Service: Finished first upload petitions to user calendar for user {}", userId);
    }

    @Override
    public void firstUploadVotingToUserCalendar(long userId) {
        log.info("Service: Starting first upload voting to user calendar for user {}", userId);
        List<Voting> myVoting = votingService.findAllByUser(userId);
        Event[] result;
        User user = userService.findById(userId);
        Event votingEvent;
        Event reminderVotingEvent;
        for(Voting v : myVoting) {
            votingEvent = GoogleCalendarEventMapper.fromVotingToEvent(v);
            reminderVotingEvent = GoogleCalendarEventMapper.fromVotingToReminderEvent(v);
            result = saveVotingToUserCalendar(votingEvent, reminderVotingEvent, userId);
            userVotingEventService.create(user, v, result[0].getId(), result[1].getId());
        }
        log.info("Service: Finished first upload voting to user calendar for user {}", userId);
    }

    @Override
    public Event[] savePetitionToUserCalendar(Event petitionEvent, Event petitionReminderEvent, long userId) {
        log.info("Service: Starting save petition event {} to user calendar for user {}", petitionEvent, userId);
        Calendar service = getCalendarService(userId);
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        Event[] result = new Event[2];
        try {
            result[1] = service.events().insert(userCalendar.getCalendarId(), petitionEvent).execute();
            result[0] = service.events().insert(userCalendar.getCalendarId(), petitionReminderEvent).execute();
            log.info("Service: Saved petition events to calendar for user {}", userId);
        } catch (IOException e) {
            log.error("Service: Failed to save petition events for user {}", userId, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Event[] saveVotingToUserCalendar(Event votingEvent, Event votingReminderEvent, long userId) {
        log.info("Service: Starting save voting event {} to user calendar for user {}", votingEvent, userId);
        Calendar service = getCalendarService(userId);
        UserCalendar userCalendar = userCalendarService.findByUser(userId);

        Event[] result = new Event[2];
        try {
            result[1] = service.events().insert(userCalendar.getCalendarId(), votingEvent).execute();
            result[0] = service.events().insert(userCalendar.getCalendarId(), votingReminderEvent).execute();
            log.info("Service: Saved voting events to calendar for user {}", userId);
        } catch (IOException e) {
            log.error("Service: Failed to save voting events for user {}", userId, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void deletePetitionFromUserCalendar(long petitionId, long userId) {
        log.info("Service: Starting deletion petition event {} to user calendar for user {}", petitionId, userId);
        Calendar service = getCalendarService(userId);
        UserPetitionEvent userPetitionEvent = userPetitionEventService.findByUserAndPetition(userId, petitionId);
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        try {
            service.events().delete(userCalendar.getCalendarId(), userPetitionEvent.getEventId()).execute();
            service.events().delete(userCalendar.getCalendarId(), userPetitionEvent.getReminderEventId()).execute();
            log.info("Service: Deleted petition events from calendar for user {} and petition {}", userId, petitionId);
        } catch (IOException e) {
            log.error("Service: Failed to delete petition events for user {} and petition {}", userId, petitionId, e);
            throw new RuntimeException(e);
        }
        log.info("Service: Deleted petition event {} to user calendar for user {}", petitionId, userId);
    }

    @Override
    public void deleteVotingFromUserCalendar(long votingId, long userId) {
        log.info("Service: Starting deletion voting event {} to user calendar for user {}", votingId, userId);
        Calendar service = getCalendarService(userId);
        UserVotingEvent userPetitionEvent = userVotingEventService.findByUserAndVoting(userId, votingId);
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        try {
            service.events().delete(userCalendar.getCalendarId(), userPetitionEvent.getEventId()).execute();
            service.events().delete(userCalendar.getCalendarId(), userPetitionEvent.getReminderEventId()).execute();
            log.info("Service: Deleted voting events from calendar for user {} and voting {}", userId, votingId);
        } catch (IOException e) {
            log.error("Service: Failed to delete voting events for user {} and voting {}", userId, votingId, e);
            throw new RuntimeException(e);
        }
        log.info("Service: Deleted voting event {} to user calendar for user {}", votingId, userId);
    }

    @Override
    public Event[] updatePetitionInUserCalendar(Event petitionEvent, Event petitionReminderEvent, long petitionId, long userId) {
        log.info("Service: Starting updating petition event {} to user calendar for user {}", petitionEvent, petitionId);
        Calendar service = getCalendarService(userId);
        UserPetitionEvent userPetitionEvent = userPetitionEventService.findByUserAndPetition(userId, petitionId);
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        Event[] result = new Event[2];
        try {
            result[0] = service.events().update(userCalendar.getCalendarId(), userPetitionEvent.getEventId(), petitionEvent).execute();
            result[1] = service.events().update(userCalendar.getCalendarId(), userPetitionEvent.getReminderEventId(), petitionReminderEvent).execute();
            log.info("Service: Updated petition events in calendar for user {} and petition {}", userId, petitionId);
        } catch (IOException e) {
            log.error("Service: Failed to update petition events for user {} and petition {}", userId, petitionId, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Event[] updateVotingInUserCalendar(Event votingEvent, Event votingReminderEvent, long votingId, long userId) {
        log.info("Service: Starting updating voting event {} to user calendar for user {}", votingEvent, votingId);
        Calendar service = getCalendarService(userId);
        UserVotingEvent userVotingEvent = userVotingEventService.findByUserAndVoting(userId, votingId);
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        Event[] result = new Event[2];
        try {
            result[0] = service.events().update(userCalendar.getCalendarId(), userVotingEvent.getEventId(), votingEvent).execute();
            result[1] = service.events().update(userCalendar.getCalendarId(), userVotingEvent.getReminderEventId(), votingReminderEvent).execute();
            log.info("Service: Updated voting events in calendar for user {} and voting {}", userId, votingId);
        } catch (IOException e) {
            log.error("Service: Failed to update voting events for user {} and voting {}", userId, votingId, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void deleteCalendar(long userId) {
        log.info("Service: Starting deletion calendar for user {}", userId);
        Calendar service = getCalendarService(userId);
        userVotingEventService.deleteByUser(userId);
        userPetitionEventService.deleteByUser(userId);
        boolean exists = userCalendarService.existsByUser(userId);
        log.info("Service: Exists for user {}: {}", userId, exists);
        if(exists) {
            try {
                log.info("Service: Deleting calendar for user with id {}", userId);
                service.calendars().delete(userCalendarService.findByUser(userId).getCalendarId()).execute();
            } catch (IOException e) {
                log.error("Service: Failed to delete calendar for user {}", userId, e);
                throw new RuntimeException(e);
            }
            userCalendarService.deleteByUser(userId);
        }
        log.info("Service: Deleted calendar for user {}", userId);
    }

    public Calendar getCalendarService(long userId) {
        log.info("Service: Getting calendar for user {}", userId);
        GoogleCalendarCredential googleCalendarCredential;
        if (googleCalendarCredentialService.existsByUserId(userId)) {
            googleCalendarCredential = googleCalendarCredentialService.refresh(userId);
        } else {
            throw new UnsupportedOperationException("User with id " + userId + " didn`t connect google calendar");
        }

        String accessToken = googleCalendarCredential.getAccessToken();
        HttpRequestInitializer requestInitializer = request -> {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        };

        try {
            return new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    requestInitializer
            ).setApplicationName("Coffee Programmers Voting Calendar").build();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePetitionToUserCalendar(Petition petition){
        log.info("Service: Starting saving petition {} to user calendars",  petition.getName());
        List<User> users = userService.findAllByPetition(petition);
        Event[] events;
        Event petitionEvent = GoogleCalendarEventMapper.fromPetitionToEvent(petition);
        Event petitionReminderEvent = GoogleCalendarEventMapper.fromPetitionToReminderEvent(petition);
        for(User user : users){
            events = savePetitionToUserCalendar(petitionEvent, petitionReminderEvent, user.getId());
            userPetitionEventService.create(user, petition, events[0].getId(), events[1].getId());
            log.info("Service: Saved petition calendar events for user {} and petition {}", user.getId(), petition.getId());
        }
    }


    @Override
    public void saveVotingToUserCalendar(Voting voting){
        log.info("Service: Starting saving voting {} to user calendars", voting.getName());
        List<User> users = userService.findAllByVoting(voting);
        Event[] events;
        Event votingEvent = GoogleCalendarEventMapper.fromVotingToEvent(voting);
        Event votingReminderEvent = GoogleCalendarEventMapper.fromVotingToReminderEvent(voting);
        for(User user : users){
            events = saveVotingToUserCalendar(votingEvent, votingReminderEvent, user.getId());
            userVotingEventService.create(user, voting, events[0].getId(), events[1].getId());
            log.info("Service: Saved voting calendar events for user {} and voting {}", user.getId(), voting.getId());
        }
    }

    @Override
    public void updatePetitionToUserCalendar(Petition petition){
        log.info("Service: Starting updating petition {} to user calendars", petition.getName());
        List<User> users = userService.findAllByPetition(petition);
        Event petitionEvent = GoogleCalendarEventMapper.fromPetitionToEvent(petition);
        Event petitionReminderEvent = GoogleCalendarEventMapper.fromPetitionToReminderEvent(petition);
        long petitionId = petition.getId();
        for(User user : users){
            updatePetitionInUserCalendar(petitionEvent, petitionReminderEvent, petitionId, user.getId());
            log.info("Service: Updated petition calendar events for user {} and petition {}", user.getId(), petitionId);
        }
    }

    @Override
    public void updateVotingToUserCalendar(Voting voting){
        log.info("Service: Starting updating voting {} to user calendars", voting.getName());
        List<User> users = userService.findAllByVoting(voting);
        Event votingEvent = GoogleCalendarEventMapper.fromVotingToEvent(voting);
        Event votingReminderEvent = GoogleCalendarEventMapper.fromVotingToReminderEvent(voting);
        long votingId = voting.getId();
        for(User user : users){
            updateVotingInUserCalendar(votingEvent, votingReminderEvent, votingId, user.getId());
            log.info("Service: Updated voting calendar events for user {} and voting {}", user.getId(), votingId);
        }
    }

    @Override
    public void deleteCalendarAndRevoke(User user) {
        log.info("Service: Deleting calendar and revoke for user {}", user.getId());
        if(googleCalendarCredentialService.existsByUserId(user.getId())) {
            deleteCalendar(user.getId());
            googleCalendarCredentialService.revokeAccess(user.getId()).subscribe();
            user.setGoogleCalendarCredential(null);
            userService.save(user);
            googleCalendarCredentialService.deleteByUser(user.getId());
            log.info("Service: Deleted calendar and revoke for user {}", user.getId());
        }
    }

    @Override
    public void deletePetitionFromUserCalendar(long petitionId) {
        log.info("Service: Deleting petition from user calendar {}", petitionId);
        Petition petition = petitionService.findById(petitionId);
        List<User> users = userService.findAllByPetition(petition);
        for(User user : users){
            deletePetitionFromUserCalendar(petitionId, user.getId());
            userPetitionEventService.delete(user.getId(), petitionId);
            log.info("Service: Deleted petition calendar events for user {} and petition {}", user.getId(), petitionId);
        }
    }

    @Override
    public void deleteVotingFromUserCalendar(long votingId) {
        log.info("Service: Deleting voting from user calendar {}", votingId);
        Voting voting = votingService.findById(votingId);
        List<User> users = userService.findAllByVoting(voting);
        for(User user : users){
            deleteVotingFromUserCalendar(votingId, user.getId());
            userVotingEventService.delete(user.getId(), votingId);
            log.info("Service: Deleted voting calendar events for user {} and voting {}", user.getId(), votingId);
        }
    }

    @Override
    public void deleteAllClassPetitionsAndVotingsFromUsers(long userId){
        log.info("Service: Deleting all petitions and votings from user {}", userId);
        if(!googleCalendarCredentialService.existsByUserId(userId)) {
            log.info("User with id {} didnt connect google calendar", userId);
            return;
        }
        List<Petition> petitions = petitionService.findAllByUserAndLevelClass(userId);
        for(Petition petition : petitions){
            log.info("Service: Deleting petition {} from user", petition.getId());
            this.deletePetitionFromUserCalendar(petition.getId(), userId);
            userPetitionEventService.delete(userId, petition.getId());
        }
        List<Voting> votings = votingService.findAllByUserAndLevelClass(userId);
        for(Voting voting : votings){
            log.info("Service: Deleting voting {} from user", voting.getId());
            this.deleteVotingFromUserCalendar(voting.getId(), userId);
            userVotingEventService.delete(userId, voting.getId());
        }
        log.info("Service: Deleted all petitions and votings from user {}", userId);
    }

    @Override
    public void saveAllClassPetitionsAndVotingsToUsers(long userId){
        log.info("Service: Saving all petitions and votings to user {}", userId);
        User user = userService.findById(userId);
        List<Petition> petitions = petitionService.findAllByClass(user.getMyClass().getId());
        Event[] result;
        for(Petition petition : petitions){
            log.info("Service: Saving petition {} to user {}", petition.getId(), user.getId());
            result = this.savePetitionToUserCalendar(
                    GoogleCalendarEventMapper.fromPetitionToEvent(petition), GoogleCalendarEventMapper.fromPetitionToReminderEvent(petition), userId);
            userPetitionEventService.create(user, petition, result[0].getId(), result[1].getId());
        }
        List<Voting> votings = votingService.findAllByUserAndLevelClass(user.getMyClass().getId());
        for(Voting voting : votings){
            log.info("Service: Saving voting {} to user {}", voting.getId(), user.getId());
            result = this.saveVotingToUserCalendar(GoogleCalendarEventMapper.fromVotingToEvent(voting), GoogleCalendarEventMapper.fromVotingToReminderEvent(voting), userId);
            userVotingEventService.create(user, voting, result[0].getId(), result[1].getId());
        }
        log.info("Service: Saved all petitions and votings to user {}", userId);
    }
}

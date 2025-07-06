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
        List<Petition> myPetitions = petitionService.findAllMy(userId);
        myPetitions.forEach(p ->
                savePetitionToUserCalendar(
                        GoogleCalendarEventMapper.fromPetitionToEvent(p),
                        GoogleCalendarEventMapper.fromPetitionToReminderEvent(p),
                        userId)
        );
    }

    @Override
    public void firstUploadVotingToUserCalendar(long userId) {
        List<Voting> myVoting = votingService.findAllByUser(userId);
        myVoting.forEach(v -> saveVotingToUserCalendar(GoogleCalendarEventMapper.fromVotingToEvent(v), GoogleCalendarEventMapper.fromVotingToReminderEvent(v), userId));
    }

    @Override
    public Event[] savePetitionToUserCalendar(Event petitionEvent, Event petitionReminderEvent, long userId) {
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
    }

    @Override
    public void deleteVotingFromUserCalendar(long votingId, long userId) {
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
    }

    @Override
    public Event[] updatePetitionInUserCalendar(Event petitionEvent, Event petitionReminderEvent, long petitionId, long userId) {
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
    }

    public Calendar getCalendarService(long userId) {
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
        if(googleCalendarCredentialService.existsByUserId(user.getId())) {
            deleteCalendar(user.getId());
            googleCalendarCredentialService.revokeAccess(user.getId()).subscribe();
            user.setGoogleCalendarCredential(null);
            userService.save(user);
            googleCalendarCredentialService.deleteByUser(user.getId());
        }
    }

    @Override
    public void deletePetitionFromUserCalendar(long petitionId) {
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
        Voting voting = votingService.findById(votingId);
        List<User> users = userService.findAllByVoting(voting);
        for(User user : users){
            deleteVotingFromUserCalendar(votingId, user.getId());
            userVotingEventService.delete(user.getId(), votingId);
            log.info("Service: Deleted voting calendar events for user {} and voting {}", user.getId(), votingId);
        }
    }
}

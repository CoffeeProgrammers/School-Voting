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
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary(name);
        calendar.setTimeZone(timeZone);

        com.google.api.services.calendar.model.Calendar created = null;
        try {
            created = service.calendars().insert(calendar).execute();
        } catch (IOException e) {
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
        Calendar service = getCalendarService(userId);
        createCalendar(service, "Coffee Programmers Voting/Petitions Calendar", "Europe/Kyiv", userId);
        firstUploadPetitionsToUserCalendar(userId);
        firstUploadVotingToUserCalendar(userId);
    }

    @Override
    public void firstUploadPetitionsToUserCalendar(long userId) {
        List<Petition> myPetitions = petitionService.findAllMy(userId);
        myPetitions.forEach(p -> savePetitionToUserCalendar(p, userId));
    }

    @Override
    public void firstUploadVotingToUserCalendar(long userId) {
        List<Voting> myVoting = votingService.findAllByUser(userId);
        myVoting.forEach(v -> saveVotingToUserCalendar(v, userId));
    }

    @Override
    public Event[] savePetitionToUserCalendar(Petition petition, long userId) {
        Calendar service = getCalendarService(userId);
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        User user = userService.findById(userId);
        Event[] result = new Event[2];
        try {
            result[0] = service.events().insert(userCalendar.getCalendarId(), GoogleCalendarEventMapper.fromPetitionToEvent(petition)).execute();
            result[1] = service.events().insert(userCalendar.getCalendarId(), GoogleCalendarEventMapper.fromPetitionToReminderEvent(petition)).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userPetitionEventService.create(user, petition, result[0].getId(), result[1].getId());
        return result;
    }

    @Override
    public Event[] saveVotingToUserCalendar(Voting voting, long userId) {
        Calendar service = getCalendarService(userId);
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        User user = userService.findById(userId);
        Event[] result = new Event[2];
        try {
            result[0] = service.events().insert(userCalendar.getCalendarId(), GoogleCalendarEventMapper.fromVotingToEvent(voting)).execute();
            result[1] = service.events().insert(userCalendar.getCalendarId(), GoogleCalendarEventMapper.fromVotingToReminderEvent(voting)).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userVotingEventService.create(user, voting, result[0].getId(), result[1].getId());
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
        } catch (IOException e) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Event[] updatePetitionInUserCalendar(Petition petition, long userId) {
        Calendar service = getCalendarService(userId);
        UserPetitionEvent userPetitionEvent = userPetitionEventService.findByUserAndPetition(userId, petition.getId());
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        Event[] result = new Event[2];
        try {
            result[0] = service.events().update(userCalendar.getCalendarId(), userPetitionEvent.getEventId(), GoogleCalendarEventMapper.fromPetitionToEvent(petition)).execute();
            result[1] = service.events().update(userCalendar.getCalendarId(), userPetitionEvent.getReminderEventId(), GoogleCalendarEventMapper.fromPetitionToReminderEvent(petition)).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Event[] updateVotingInUserCalendar(Voting voting, long userId) {
        Calendar service = getCalendarService(userId);
        UserVotingEvent userVotingEvent = userVotingEventService.findByUserAndVoting(userId, voting.getId());
        UserCalendar userCalendar = userCalendarService.findByUser(userId);
        Event[] result = new Event[2];
        try {
            result[0] = service.events().update(userCalendar.getCalendarId(), userVotingEvent.getEventId(), GoogleCalendarEventMapper.fromVotingToEvent(voting)).execute();
            result[1] = service.events().update(userCalendar.getCalendarId(), userVotingEvent.getReminderEventId(), GoogleCalendarEventMapper.fromVotingToReminderEvent(voting)).execute();
        } catch (IOException e) {
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
        log.info("Exists for user {}: {}", userId, exists);
        if(exists) {
            try {
                log.info("Deleting calendar for user with id {}", userId);
                service.calendars().delete(userCalendarService.findByUser(userId).getCalendarId()).execute();
            } catch (IOException e) {
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


}

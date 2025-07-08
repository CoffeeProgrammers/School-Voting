package com.project.backend.services.impl.google;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.project.backend.TestUtil;
import com.project.backend.models.User;
import com.project.backend.models.google.GoogleCalendarCredential;
import com.project.backend.models.google.UserCalendar;
import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.Voting;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.google.GoogleCalendarCredentialService;
import com.project.backend.services.inter.google.UserCalendarService;
import com.project.backend.services.inter.google.UserPetitionEventService;
import com.project.backend.services.inter.google.UserVotingEventService;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleCalendarServiceImplTest {

    @Mock UserService userService;
    @Mock UserCalendarService userCalendarService;
    @Mock UserPetitionEventService userPetitionEventService;
    @Mock UserVotingEventService userVotingEventService;
    @Mock GoogleCalendarCredentialService googleCalendarCredentialService;
    @Mock PetitionService petitionService;
    @Mock VotingService votingService;

    @Mock Calendar calendarService;
    @Mock Calendar.Calendars calendars;
    @Mock Calendar.Calendars.Insert calendarsInsert;

    @InjectMocks GoogleCalendarServiceImpl googleCalendarService;

    @Test
    void createCalendar_shouldCreateAndSaveUserCalendar() throws IOException {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userService.findById(userId)).thenReturn(user);
        when(calendarService.calendars()).thenReturn(calendars);
        when(calendars.insert(any())).thenReturn(calendarsInsert);
        when(calendarsInsert.execute()).thenReturn(new com.google.api.services.calendar.model.Calendar().setId("calId"));

        com.google.api.services.calendar.model.Calendar created = googleCalendarService.createCalendar(calendarService, "My Calendar", "UTC", userId);

        assertNotNull(created);
        assertEquals("calId", created.getId());
        verify(userCalendarService).create(any(UserCalendar.class));
    }

    @Test
    void firstUploadToUserCalendar_shouldCallUploadsAndCreateCalendar() {
        long userId = 1L;

        GoogleCalendarServiceImpl spyService = Mockito.spy(googleCalendarService);

        doReturn(calendarService).when(spyService).getCalendarService(userId);
        doReturn(new com.google.api.services.calendar.model.Calendar().setId("calId")).when(spyService).createCalendar(any(), anyString(), anyString(), anyLong());
        doNothing().when(spyService).firstUploadPetitionsToUserCalendar(userId);
        doNothing().when(spyService).firstUploadVotingToUserCalendar(userId);

        spyService.firstUploadToUserCalendar(userId);

        verify(spyService).createCalendar(any(), eq("Coffee Programmers Voting/Petitions Calendar"), eq("Europe/Kyiv"), eq(userId));
        verify(spyService).firstUploadPetitionsToUserCalendar(userId);
        verify(spyService).firstUploadVotingToUserCalendar(userId);
    }

    @Test
    void firstUploadPetitionsToUserCalendar_shouldSavePetitionsEvents() {
        long userId = 1L;
        Petition petition = TestUtil.createPetition("Some");

        when(petitionService.findAllMy(userId)).thenReturn(List.of(petition));
        when(userService.findById(userId)).thenReturn(new User());

        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        //doReturn(calendarService).when(spy).getCalendarService(userId);
        doReturn(new Event[]{new Event().setId("e1"), new Event().setId("e2")}).when(spy).savePetitionToUserCalendar(any(), any(), eq(userId));
        when(userPetitionEventService.create(any(), any(), anyString(), anyString())).thenReturn(any());

        spy.firstUploadPetitionsToUserCalendar(userId);

        verify(userPetitionEventService).create(any(), eq(petition), eq("e1"), eq("e2"));
    }

    @Test
    void firstUploadVotingToUserCalendar_shouldSaveVotingEvents() {
        long userId = 1L;
        Voting voting = TestUtil.createVoting("Some");

        when(votingService.findAllByUser(userId)).thenReturn(List.of(voting));
        when(userService.findById(userId)).thenReturn(new User());

        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        //doReturn(calendarService).when(spy).getCalendarService(userId);
        doReturn(new Event[]{new Event().setId("e1"), new Event().setId("e2")}).when(spy).saveVotingToUserCalendar(any(), any(), eq(userId));
        when(userVotingEventService.create(any(), any(), anyString(), anyString())).thenReturn(any());

        spy.firstUploadVotingToUserCalendar(userId);

        verify(userVotingEventService).create(any(), eq(voting), eq("e1"), eq("e2"));
    }

    @Test
    void getCalendarService_shouldReturnCalendar() throws GeneralSecurityException, IOException {
        long userId = 1L;
        GoogleCalendarCredential cred = mock(GoogleCalendarCredential.class);

        when(googleCalendarCredentialService.existsByUserId(userId)).thenReturn(true);
        when(googleCalendarCredentialService.refresh(userId)).thenReturn(cred);
        when(cred.getAccessToken()).thenReturn("token");

        Calendar cal = googleCalendarService.getCalendarService(userId);

        assertNotNull(cal);
    }

    @Test
    void getCalendarService_shouldThrowIfNoCredential() {
        long userId = 1L;

        when(googleCalendarCredentialService.existsByUserId(userId)).thenReturn(false);

        assertThrows(UnsupportedOperationException.class, () -> googleCalendarService.getCalendarService(userId));
    }

    @Test
    void savePetitionToUserCalendar_shouldIterateUsers() {
        Petition petition = TestUtil.createPetition("Some");
        petition.setId(1L);

        User user = new User();
        user.setId(10L);

        when(userService.findAllByPetition(petition)).thenReturn(List.of(user));
        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        //doReturn(calendarService).when(spy).getCalendarService(user.getId());
        doReturn(new Event[]{new Event().setId("e1"), new Event().setId("e2")}).when(spy).savePetitionToUserCalendar(any(), any(), eq(user.getId()));
        when(userPetitionEventService.create(any(), any(), anyString(), anyString())).thenReturn(any());

        spy.savePetitionToUserCalendar(petition);

        verify(userPetitionEventService).create(user, petition, "e1", "e2");
    }

    @Test
    void saveVotingToUserCalendar_shouldIterateUsers() {
        Voting voting = TestUtil.createVoting("Some");
        voting.setId(1L);

        User user = new User();
        user.setId(10L);

        when(userService.findAllByVoting(voting)).thenReturn(List.of(user));
        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        //doReturn(calendarService).when(spy).getCalendarService(user.getId());
        doReturn(new Event[]{new Event().setId("e1"), new Event().setId("e2")}).when(spy).saveVotingToUserCalendar(any(), any(), eq(user.getId()));
        when(userVotingEventService.create(any(), any(), anyString(), anyString())).thenReturn(any());

        spy.saveVotingToUserCalendar(voting);

        verify(userVotingEventService).create(user, voting, "e1", "e2");
    }

    @Test
    void updatePetitionToUserCalendar_shouldIterateUsers() {
        Petition petition = TestUtil.createPetition("Some");
        petition.setId(1L);

        User user = new User();
        user.setId(10L);

        when(userService.findAllByPetition(petition)).thenReturn(List.of(user));
        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        //doReturn(calendarService).when(spy).getCalendarService(user.getId());
        doReturn(new Event[]{new Event().setId("e1"), new Event().setId("e2")}).when(spy).updatePetitionInUserCalendar(any(), any(), eq(petition.getId()), eq(user.getId()));

        spy.updatePetitionToUserCalendar(petition);

        verify(spy).updatePetitionInUserCalendar(any(), any(), eq(petition.getId()), eq(user.getId()));
    }

    @Test
    void updateVotingToUserCalendar_shouldIterateUsers() {
        Voting voting = TestUtil.createVoting("Some");
        voting.setId(1L);

        User user = new User();
        user.setId(10L);

        when(userService.findAllByVoting(voting)).thenReturn(List.of(user));
        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        //doReturn(calendarService).when(spy).getCalendarService(user.getId());
        doReturn(new Event[]{new Event().setId("e1"), new Event().setId("e2")}).when(spy).updateVotingInUserCalendar(any(), any(), eq(voting.getId()), eq(user.getId()));

        spy.updateVotingToUserCalendar(voting);

        verify(spy).updateVotingInUserCalendar(any(), any(), eq(voting.getId()), eq(user.getId()));
    }

    @Test
    void deletePetitionFromUserCalendar_shouldIterateUsers() {
        Petition petition = new Petition();
        petition.setId(1L);
        User user = new User();
        user.setId(10L);

        when(petitionService.findById(petition.getId())).thenReturn(petition);
        when(userService.findAllByPetition(petition)).thenReturn(List.of(user));

        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        doNothing().when(spy).deletePetitionFromUserCalendar(petition.getId(), user.getId());
        doNothing().when(userPetitionEventService).delete(user.getId(), petition.getId());

        spy.deletePetitionFromUserCalendar(petition.getId());

        verify(spy).deletePetitionFromUserCalendar(petition.getId(), user.getId());
        verify(userPetitionEventService).delete(user.getId(), petition.getId());
    }

    @Test
    void deleteVotingFromUserCalendar_shouldIterateUsers() {
        Voting voting = new Voting();
        voting.setId(1L);
        User user = new User();
        user.setId(10L);

        when(votingService.findById(voting.getId())).thenReturn(voting);
        when(userService.findAllByVoting(voting)).thenReturn(List.of(user));

        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        doNothing().when(spy).deleteVotingFromUserCalendar(voting.getId(), user.getId());
        doNothing().when(userVotingEventService).delete(user.getId(), voting.getId());

        spy.deleteVotingFromUserCalendar(voting.getId());

        verify(spy).deleteVotingFromUserCalendar(voting.getId(), user.getId());
        verify(userVotingEventService).delete(user.getId(), voting.getId());
    }

    @Test
    void deleteAllClassPetitionsAndVotingsFromUsers_shouldDeleteAll() {
        long userId = 1L;
        Petition petition = new Petition();
        petition.setId(1L);
        Voting voting = new Voting();
        voting.setId(1L);

        when(googleCalendarCredentialService.existsByUserId(userId)).thenReturn(true);
        when(petitionService.findAllByUserAndLevelClass(userId)).thenReturn(List.of(petition));
        when(votingService.findAllByUserAndLevelClass(userId)).thenReturn(List.of(voting));

        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        doNothing().when(spy).deletePetitionFromUserCalendar(anyLong(), eq(userId));
        doNothing().when(userPetitionEventService).delete(userId, petition.getId());
        doNothing().when(spy).deleteVotingFromUserCalendar(anyLong(), eq(userId));
        doNothing().when(userVotingEventService).delete(userId, voting.getId());

        spy.deleteAllClassPetitionsAndVotingsFromUsers(userId);

        verify(spy).deletePetitionFromUserCalendar(petition.getId(), userId);
        verify(userPetitionEventService).delete(userId, petition.getId());

        verify(spy).deleteVotingFromUserCalendar(voting.getId(), userId);
        verify(userVotingEventService).delete(userId, voting.getId());
    }

    @Test
    void saveAllClassPetitionsAndVotingsToUsers_shouldSaveAll() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setMyClass(TestUtil.createClass("s"));

        Petition petition = TestUtil.createPetition("S");
        petition.setId(1L);

        Voting voting = TestUtil.createVoting("S");
        voting.setId(1L);

        when(userService.findById(userId)).thenReturn(user);
        when(petitionService.findAllByClass(anyLong())).thenReturn(List.of(petition));
        when(votingService.findAllByUserAndLevelClass(anyLong())).thenReturn(List.of(voting));

        GoogleCalendarServiceImpl spy = spy(googleCalendarService);
        doReturn(new Event[]{new Event().setId("e1"), new Event().setId("e2")}).when(spy).savePetitionToUserCalendar(any(), any(), eq(userId));
        doReturn(new Event[]{new Event().setId("v1"), new Event().setId("v2")}).when(spy).saveVotingToUserCalendar(any(), any(), eq(userId));

        when(userPetitionEventService.create(any(), any(), anyString(), anyString()))
                .thenReturn(new UserPetitionEvent());
        when(userVotingEventService.create(any(), any(), anyString(), anyString()))
                .thenReturn(new UserVotingEvent());  // або null, якщо клас UserVotingEvent є, інакше new UserVotingEvent()

        spy.saveAllClassPetitionsAndVotingsToUsers(userId);

        verify(userPetitionEventService).create(user, petition, "e1", "e2");
        verify(userVotingEventService).create(user, voting, "v1", "v2");
    }
}

package com.project.backend.services.impl;

import com.google.api.services.calendar.model.Event;
import com.project.backend.models.petitions.Petition;
import com.project.backend.models.voting.Voting;
import com.project.backend.services.inter.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {
    private final UserService userService;
    private final GoogleCalendarCredentialService googleCalendarCredentialService;
    private final PetitionService petitionService;
    private final VotingService votingService;
    @Override
    public List<Event> firstUploadPetitionsToUserCalendar(long userId) {
        List<Petition> myPetitions = petitionService.findAllMy(userId);
        myPetitions

        return List.of();
    }

    @Override
    public List<Event> firstUploadVotingToUserCalendar(long userId) {
        return List.of();
    }

    @Override
    public Event savePetitionToUserCalendar(Petition petition, long userId) {
        return null;
    }

    @Override
    public Event saveVotingToUserCalendar(Voting voting, long userId) {
        return null;
    }

    @Override
    public void deletePetitionFromUserCalendar(long petitionId, long userId) {

    }

    @Override
    public void deleteVotingFromUserCalendar(long votingId, long userId) {

    }

    @Override
    public Event updatePetitionInUserCalendar(Petition petition, long userId) {
        return null;
    }

    @Override
    public Event updateVotingInUserCalendar(Voting voting, long userId) {
        return null;
    }
}

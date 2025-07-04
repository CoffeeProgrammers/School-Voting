package com.project.backend.controllers;

import com.project.backend.dto.voting.*;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.mappers.petition.AnswerMapper;
import com.project.backend.mappers.voting.VotingMapper;
import com.project.backend.models.User;
import com.project.backend.models.voting.Voting;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.voting.AnswerService;
import com.project.backend.services.inter.voting.VotingService;
import com.project.backend.services.inter.voting.VotingUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/schools/{school_id}/votings")
@RequiredArgsConstructor
public class VotingController {
    private final VotingService votingService;
    private final VotingUserService votingUserService;
    private final AnswerService answerService;
    private final UserService userService;
    private final VotingMapper votingMapper;
    private final AnswerMapper answerMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId)")
    public VotingFullResponse create(@PathVariable("school_id") Long schoolId,
                                     @Valid @RequestBody VotingCreateRequest votingCreateRequest,
                                     Authentication auth) {
        log.info("Controller: Create vote with body {}", votingCreateRequest);
        Voting createdVoting = votingService.create(votingMapper.fromRequestToVoting(votingCreateRequest), votingCreateRequest.getAnswers(), votingCreateRequest.getTargetIds(), schoolId, userService.findUserByAuth(auth).getId());
        return fromVotingToFullResponseWithStatistics(createdVoting);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and @userSecurity.checkCreatorVoting(#auth, #votingId)")
    @PutMapping("/update/{voting_id}")
    @ResponseStatus(HttpStatus.OK)
    public VotingFullResponse update(@PathVariable("school_id") Long schoolId,
                                     @PathVariable("voting_id") Long votingId,
                                     @Valid  @RequestBody VotingUpdateRequest votingUpdateRequest,
                                     Authentication auth) {
        log.info("Controller: Update vote with id {} with body {}", votingId, votingUpdateRequest);
        Voting updatedVoting = votingService.update(votingMapper.fromRequestToVoting(votingUpdateRequest), votingUpdateRequest.getAnswers(), votingId);
        return fromVotingToFullResponseWithStatistics(updatedVoting);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and @userSecurity.checkCreatorVoting(#auth, #votingId)")
    @DeleteMapping("/delete/{voting_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("school_id") Long schoolId,
                       @PathVariable("voting_id") Long votingId,
                       Authentication auth) {
        log.info("Controller: Delete voting with id {}", votingId);
        votingService.delete(votingId);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<VotingListResponse> getAllMy
            (@PathVariable("school_id") Long schoolId,
             @RequestParam Integer page,
             @RequestParam Integer size,
             @RequestParam(required = false) String name,
             @RequestParam(required = false) Boolean now,
             @RequestParam(required = false) Boolean isNotVote,
             Authentication auth) {
        User user = userService.findUserByAuth(auth);
        log.info("Controller: Get all votings for user {}", user.getEmail());
        Page<Voting> votingPage = votingService.findAllByUser(user.getId(), name, now, isNotVote, page, size);
        PaginationListResponse<VotingListResponse> response = new PaginationListResponse<>();
        response.setTotalPages(votingPage.getTotalPages());
        response.setContent(votingPage.getContent().stream().map(v -> {
            VotingListResponse res = votingMapper.fromVotingToListResponse(v);
            res.setIsAnswered(votingUserService.findById(res.getId(), user.getId()).getAnswer() != null);
            return res;
        }).toList());
        return response;
    }

    @GetMapping("/createdByMe")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<VotingListResponse> getAllCreatedByMe(
            @PathVariable("school_id") Long schoolId,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean now,
            @RequestParam(required = false) Boolean notStarted,
            Authentication auth) {
        User user = userService.findUserByAuth(auth);
        log.info("Controller: Get all votings created by user {}", user.getEmail());
        Page<Voting> votingPage = votingService.findAllByCreator(user.getId(), name, now, notStarted, page, size);
        PaginationListResponse<VotingListResponse> response = new PaginationListResponse<>();
        response.setTotalPages(votingPage.getTotalPages());
        response.setContent(votingPage.getContent().stream().map(votingMapper::fromVotingToListResponse).toList());
        return response;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('DIRECTOR')")
    @GetMapping("/forDirector")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<VotingListResponse> getAllForDirector(
            @PathVariable("school_id") Long schoolId,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) String name,
            Authentication auth) {
        User user = userService.findUserByAuth(auth);
        log.info("Controller: Get all votings for director {}, role {}", user.getEmail(), user.getRole());
        Page<Voting> votingPage = votingService.findAllForDirector(user.getId(), name, page, size);
        PaginationListResponse<VotingListResponse> response = new PaginationListResponse<>();
        response.setTotalPages(votingPage.getTotalPages());
        response.setContent(votingPage.getContent().stream().map(votingMapper::fromVotingToListResponse).toList());
        return response;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and (@userSecurity.checkUserVoting(#auth, #votingId) or @userSecurity.checkCreatorVoting(#auth, #votingId))")
    @GetMapping("/{voting_id}")
    @ResponseStatus(HttpStatus.OK)
    public VotingFullResponse getById(@PathVariable("school_id") Long schoolId,
                                      @PathVariable("voting_id") Long votingId,
                                      Authentication auth) {
        log.info("Controller: Get voting by id {}", votingId);
        Voting voting = votingService.findById(votingId);
        VotingFullResponse response = fromVotingToFullResponseWithStatistics(voting);
        return response;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and @userSecurity.checkUserVoting(#auth, #votingId)")
    @PostMapping("/{voting_id}/vote/{answer_id}")
    @ResponseStatus(HttpStatus.OK)
    public void vote(@PathVariable("school_id") Long schoolId,
                     @PathVariable("voting_id") Long votingId,
                     @PathVariable("answer_id") Long answerId,
                     Authentication auth) {
        log.info("Controller: vote for answer {} in voting {}", answerId, votingId);
        votingService.vote(votingId, answerId, userService.findUserByAuth(auth));
    }

    private VotingFullResponse fromVotingToFullResponseWithStatistics(Voting voting) {
        log.info("Controller: Mapping voting {} to voting full response with statistics", voting.getId());
        VotingFullResponse response = votingMapper.fromVotingToFullResponse(voting);
        response.setStatistics(fromVotingToStatisticsResponse(voting));
        return response;
    }

    private VotingStatisticsResponse fromVotingToStatisticsResponse(Voting voting) {
        long votingId = voting.getId();
        VotingStatisticsResponse votingStatisticsResponse = new VotingStatisticsResponse();
        votingStatisticsResponse.setAnswers(answerService.findAllByVoting(votingId).stream().map(answerMapper::fromAnswerToResponse).toList());
        votingStatisticsResponse.setCountAll(votingUserService.countAllByVoting(votingId));
        votingStatisticsResponse.setCountAnswered(votingUserService.countAllByVotingAnswered(votingId));
        return votingStatisticsResponse;
    }
}

package com.project.backend.controllers;

import com.project.backend.dto.voting.*;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.mappers.AnswerMapper;
import com.project.backend.mappers.VotingMapper;
import com.project.backend.models.User;
import com.project.backend.models.voting.Voting;
import com.project.backend.services.inter.AnswerService;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.VotingService;
import com.project.backend.services.inter.VotingUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public VotingFullResponse create(@PathVariable("school_id") Long schoolId,
                                     VotingCreateRequest votingCreateRequest,
                                     Authentication authentication) {
        Voting createdVoting = votingService.create(votingMapper.fromRequestToVoting(votingCreateRequest), votingCreateRequest.getAnswers(), votingCreateRequest.getTargetIds(), schoolId, authentication);
        return fromVotingToFullResponseWithStatistics(createdVoting);
    }

    @PutMapping("/update/{voting_id}")
    @ResponseStatus(HttpStatus.OK)
    public VotingFullResponse update(@PathVariable("school_id") Long schoolId,
                                     @PathVariable("voting_id") Long votingId,
                                     VotingUpdateRequest votingUpdateRequest) {
        Voting updatedVoting = votingService.update(votingMapper.fromRequestToVoting(votingUpdateRequest), votingUpdateRequest.getAnswers(), votingId);
        return fromVotingToFullResponseWithStatistics(updatedVoting);
    }

    @DeleteMapping("/delete/{voting_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("school_id") Long schoolId,
                       @PathVariable("voting_id") Long votingId) {
        votingService.delete(votingId);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<VotingListResponse> getAllMy(@PathVariable("school_id") Long schoolId,
                                                               @RequestParam Integer page,
                                                               @RequestParam Integer size,
                                                               @RequestParam(required = false) String name,
                                                               @RequestParam(required = false) Boolean now,
                                                               @RequestParam(required = false) Boolean canVote,
                                                               Authentication authentication) {
        User user = userService.findUserByAuth(authentication);
        Page<Voting> votingPage = votingService.findAllByUser(user.getId(), name, now, canVote, page, size);
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
    public PaginationListResponse<VotingListResponse> getAllCreatedByMe(@PathVariable("school_id") Long schoolId,
                                                               @RequestParam Integer page,
                                                               @RequestParam Integer size,
                                                               @RequestParam(required = false) String name,
                                                               @RequestParam(required = false) Boolean now,
                                                               Authentication authentication) {
        User user = userService.findUserByAuth(authentication);
        Page<Voting> votingPage = votingService.findAllByCreator(user.getId(), name, now, page, size);
        PaginationListResponse<VotingListResponse> response = new PaginationListResponse<>();
        response.setTotalPages(votingPage.getTotalPages());
        response.setContent(votingPage.getContent().stream().map(votingMapper::fromVotingToListResponse).toList());
        return response;
    }

    @GetMapping("/forDirector")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<VotingListResponse> getAllForDirector(@PathVariable("school_id") Long schoolId,
                                                                        @RequestParam Integer page,
                                                                        @RequestParam Integer size,
                                                                        @RequestParam(required = false) String name,
                                                                        @RequestParam(required = false) Boolean now,
                                                                        Authentication authentication) {
        User user = userService.findUserByAuth(authentication);
        Page<Voting> votingPage = votingService.findAllForDirector(user.getId(), name, page, size);
        PaginationListResponse<VotingListResponse> response = new PaginationListResponse<>();
        response.setTotalPages(votingPage.getTotalPages());
        response.setContent(votingPage.getContent().stream().map(votingMapper::fromVotingToListResponse).toList());
        return response;
    }

    @GetMapping("/{voting_id}")
    @ResponseStatus(HttpStatus.OK)
    public VotingFullResponse getById(@PathVariable("school_id") Long schoolId,
                                                              @PathVariable("voting_id") Long votingId,
                                                              Authentication authentication) {
        Voting voting = votingService.findById(votingId);
        VotingFullResponse response = fromVotingToFullResponseWithStatistics(voting);
        return response;
    }

    @PostMapping("/{voting_id}/vote/{answer_id}")
    @ResponseStatus(HttpStatus.OK)
    public void vote(@PathVariable("school_id") Long schoolId,
                                      @PathVariable("voting_id") Long votingId,
                                      @PathVariable("answer_id") Long answerId,
                                      Authentication authentication) {
        votingService.vote(votingId, answerId, authentication);
    }

    private VotingFullResponse fromVotingToFullResponseWithStatistics(Voting voting) {
        Long votingId = voting.getId();
        VotingFullResponse response = votingMapper.fromVotingToFullResponse(voting);
        VotingStatisticsResponse votingStatisticsResponse = new VotingStatisticsResponse();
        votingStatisticsResponse.setAnswers(answerService.findAllByVoting(votingId).stream().map(answerMapper::fromAnswerToResponse).toList());
        votingStatisticsResponse.setCountAll(votingUserService.countAllByVoting(votingId));
        votingStatisticsResponse.setCountAnswered(votingUserService.countAllByVotingAnswered(votingId));
        response.setStatistics(votingStatisticsResponse);
        return response;
    }
}

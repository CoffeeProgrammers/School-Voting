package com.project.backend.controllers;

import com.project.backend.dto.comment.CommentRequest;
import com.project.backend.dto.comment.CommentResponse;
import com.project.backend.dto.petition.PetitionFullResponse;
import com.project.backend.dto.petition.PetitionListResponse;
import com.project.backend.dto.petition.PetitionRequest;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.mappers.petition.CommentMapper;
import com.project.backend.mappers.petition.PetitionMapper;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
import com.project.backend.models.petition.Comment;
import com.project.backend.models.petition.Petition;
import com.project.backend.services.inter.ClassService;
import com.project.backend.services.inter.SchoolService;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.google.GoogleCalendarService;
import com.project.backend.services.inter.petition.CommentService;
import com.project.backend.services.inter.petition.PetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/schools/{school_id}/petitions")
@RequiredArgsConstructor
public class PetitionController {

    private final GoogleCalendarService googleCalendarService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SchoolService schoolService;
    private final ClassService classService;
    private final PetitionService petitionService;
    private final PetitionMapper petitionMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final UserService userService;

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('STUDENT')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PetitionFullResponse createPetition(
            @PathVariable(name = "school_id") long schoolId,
            @Valid @RequestBody PetitionRequest petitionRequest,
            Authentication auth) {
        log.info("Controller: Creating a petition for school {}", schoolId);
        User user = userService.findUserByAuth(auth);
        LevelType levelType = LevelType.valueOf(petitionRequest.getLevelType());
        long targetId = petitionRequest.getLevelId();
        Petition petition = petitionService.create(petitionMapper.fromRequestToPetition(petitionRequest), targetId, user,
                levelType == LevelType.SCHOOL ? schoolService.findById(targetId).getName() : classService.findById(targetId).getName());
        googleCalendarService.savePetitionToUserCalendar(petition);
        return this.fromPetitionToPetitionFullResponseWithAllInfo(petition, user);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and @userSecurity.checkCreatorPetition(#auth, #petitionId)")
    @DeleteMapping("/delete/{petition_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePetition(@PathVariable(name = "school_id") long schoolId,
                               @PathVariable(name = "petition_id") long petitionId,
                               Authentication auth) {
        log.info("Controller: Deleting a petition for school {}", schoolId);
        googleCalendarService.deletePetitionFromUserCalendar(petitionId);
        petitionService.delete(petitionId);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'STUDENT')")
    @GetMapping("/{petition_id}")
    @ResponseStatus(HttpStatus.OK)
    public PetitionFullResponse getPetition(
            @PathVariable(name = "school_id") long schoolId,
            @PathVariable(name = "petition_id") long petitionId,
            Authentication auth) {
        log.info("Controller: Getting a petition with id {}", petitionId);
        User user = userService.findUserByAuth(auth);
        return this.fromPetitionToPetitionFullResponseWithAllInfo(
                petitionService.findById(petitionId), user);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('STUDENT')")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<PetitionListResponse> getMyPetitions(
            @PathVariable(name = "school_id") long schoolId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            Authentication auth) {
        log.info("Controller: Getting all my petitions");
        User user = userService.findUserByAuth(auth);
        Page<Petition> petitionPage = petitionService.findAllMy(name, status, page, size, user.getId());
        PaginationListResponse<PetitionListResponse> response = new PaginationListResponse<>();
        response.setContent(petitionPage.getContent().stream().map((Petition petition) -> fromPetitionToPetitionListResponseWithAllInfo(petition, user)).toList());
        response.setTotalPages(petitionPage.getTotalPages());
        return response;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('STUDENT')")
    @GetMapping("/createdByMe")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<PetitionListResponse> getMyOwnPetitions(
            @PathVariable(name = "school_id") long schoolId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            Authentication auth) {
        log.info("Controller: Getting all my created petitions");
        User user = userService.findUserByAuth(auth);
        Page<Petition> petitionPage = petitionService.findAllByCreator(name, status, page, size, user.getId());
        PaginationListResponse<PetitionListResponse> response = new PaginationListResponse<>();
        response.setContent(petitionPage.getContent().stream().map((Petition petition) -> fromPetitionToPetitionListResponseWithAllInfo(petition, user)).toList());
        response.setTotalPages(petitionPage.getTotalPages());
        return response;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('DIRECTOR')")
    @GetMapping("/forDirector")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<PetitionListResponse> getPetitionsForDirector(
            @PathVariable(name = "school_id") long schoolId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            Authentication auth) {
        log.info("Controller: Getting all petitions for director");
        Page<Petition> petitionPage = petitionService.findAllForDirector(name, status, schoolId, page, size);
        PaginationListResponse<PetitionListResponse> response = new PaginationListResponse<>();
        response.setContent(petitionPage.getContent().stream().map((Petition petition) -> fromPetitionToPetitionListResponseWithAllInfo(petition, userService.findUserByAuth(auth))).toList());
        response.setTotalPages(petitionPage.getTotalPages());
        return response;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('STUDENT') and @userSecurity.checkUserPetition(#auth, #petitionId)")
    @PostMapping("/support/{petition_id}")
    @ResponseStatus(HttpStatus.OK)
    public void supportPetition(@PathVariable(name = "school_id") long schoolId,
                                @PathVariable(name = "petition_id") long petitionId,
                                Authentication auth) {
        log.info("Controller: Support petition with id {}", petitionId);
        long newCount = petitionService.support(petitionId, userService.findUserByAuth(auth));
        messagingTemplate.convertAndSend("/topic/petitions/" + petitionId + "/counter", newCount);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('DIRECTOR')")
    @PostMapping("/approve/{petition_id}")
    @ResponseStatus(HttpStatus.OK)
    public void approvePetition(@PathVariable(name = "school_id") long schoolId,
                                @PathVariable(name = "petition_id") long petitionId,
                                Authentication auth) {
        log.info("Controller: Approve petition with id {}", petitionId);
        petitionService.approve(petitionId);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('DIRECTOR')")
    @PostMapping("/reject/{petition_id}")
    @ResponseStatus(HttpStatus.OK)
    public void rejectPetition(@PathVariable(name = "school_id") long schoolId,
                               @PathVariable(name = "petition_id") long petitionId,
                               Authentication auth) {
        log.info("Controller: Reject petition with id {}", petitionId);
        petitionService.reject(petitionId);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('STUDENT') and @userSecurity.checkUserPetition(#auth, #petitionId)")
    @PostMapping("/{petition_id}/comments/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@PathVariable(name = "school_id") long schoolId,
                                         @PathVariable(name = "petition_id") long petitionId,
                                         @Valid @RequestBody CommentRequest commentRequest,
                                         Authentication auth) {
        log.info("Controller: Creating a new comment for petition with id {}", petitionId);
        return commentMapper.fromCommentToResponse(
                commentService.create(commentMapper.fromRequestToComment(commentRequest), userService.findUserByAuth(auth), petitionService.findById(petitionId)));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('STUDENT') and @userSecurity.checkCreatorComment(#auth, #commentId)")
    @PutMapping("/{petition_id}/comments/update/{comment_id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse updateComment(@PathVariable(name = "school_id") long schoolId,
                                         @PathVariable(name = "petition_id") long petitionId,
                                         @PathVariable(name = "comment_id") long commentId,
                                         @Valid @RequestBody CommentRequest commentRequest,
                                         Authentication auth) {
        log.info("Controller: Updating a new comment for petition with id {}", petitionId);
        return commentMapper.fromCommentToResponse(
                commentService.update(commentMapper.fromRequestToComment(commentRequest), commentId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('STUDENT') and @userSecurity.checkCreatorComment(#auth, #commentId)")
    @DeleteMapping("/{petition_id}/comments/delete/{comment_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(name = "school_id") long schoolId,
                              @PathVariable(name = "petition_id") long petitionId,
                              @PathVariable(name = "comment_id") long commentId,
                              Authentication auth) {
        log.info("Controller: Deleting a new comment for petition with id {}", petitionId);
        commentService.delete(commentId);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('STUDENT') and @userSecurity.checkUserPetition(#auth, #petitionId)")
    @GetMapping("/{petition_id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<CommentResponse> getAllByPetition(
            @PathVariable(name = "school_id") long schoolId,
            @PathVariable(name = "petition_id") long petitionId,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth) {
        log.info("Controller: Getting all the comments for petition with id {}", petitionId);
        Page<Comment> commentPage = commentService.findAllByPetition(petitionId, page, size);
        PaginationListResponse<CommentResponse> response = new PaginationListResponse<>();
        response.setContent(commentPage.getContent().stream().map(commentMapper::fromCommentToResponse).toList());
        response.setTotalPages(commentPage.getTotalPages());
        return response;
    }

    private PetitionListResponse fromPetitionToPetitionListResponseWithAllInfo(Petition petition, User user) {
        PetitionListResponse petitionListResponse = petitionMapper.fromPetitionToListResponse(petition);
        petitionListResponse.setCountNeeded(petition.getStatus().equals(Status.ACTIVE) ? petitionService.countAll(petition) : petition.getCountNeeded());
        petitionListResponse.setCountSupported(petition.getCount());
        petitionListResponse.setSupportedByCurrentId(petition.getUsers().contains(user));
        return petitionListResponse;
    }

    private PetitionFullResponse fromPetitionToPetitionFullResponseWithAllInfo(Petition petition, User user) {
        PetitionFullResponse petitionFullResponse = petitionMapper.fromPetitionToFullResponse(petition);
        petitionFullResponse.setCountNeeded(petition.getStatus().equals(Status.ACTIVE) ? petitionService.countAll(petition) : petition.getCountNeeded());
        petitionFullResponse.setCountSupported(petition.getCount());
        petitionFullResponse.setSupportedByCurrentId(petition.getUsers().contains(user));
        return petitionFullResponse;
    }
}

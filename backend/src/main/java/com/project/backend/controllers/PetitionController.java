package com.project.backend.controllers;

import com.project.backend.dto.comment.CommentRequest;
import com.project.backend.dto.comment.CommentResponse;
import com.project.backend.dto.petition.PetitionFullResponse;
import com.project.backend.dto.petition.PetitionRequest;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.mappers.CommentMapper;
import com.project.backend.mappers.PetitionMapper;
import com.project.backend.models.petitions.Comment;
import com.project.backend.models.petitions.Petition;
import com.project.backend.services.inter.CommentService;
import com.project.backend.services.inter.PetitionService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/schools/{school_id}/petitions")
@RequiredArgsConstructor
public class PetitionController {
    private final PetitionService petitionService;
    private final PetitionMapper petitionMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PetitionFullResponse createPetition(
            @PathVariable(name = "school_id") long schoolId,
            @RequestBody PetitionRequest petitionRequest,
            Authentication auth) {
        log.info("Controller: Creating a petition for school {}", schoolId);
        return petitionMapper.fromPetitionToFullResponse(
                petitionService.create(petitionMapper.fromRequestToPetition(petitionRequest), petitionRequest.getLevelId(), auth));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PetitionFullResponse getPetition(
            @PathVariable(name = "school_id") long schoolId,
            @PathVariable(name = "id") long id,
            @RequestBody PetitionRequest petitionRequest) {
        log.info("Controller: Getting a petition with id {}", id);
        return petitionMapper.fromPetitionToFullResponse(
                petitionService.findById(id));
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<PetitionFullResponse> getMyPetitions(
            @PathVariable(name = "school_id") long schoolId,
            @PathParam("page") int page,
            @PathParam("size") int size,
            @PathParam("name") String name,
            @PathParam("status") String status,
            Authentication auth) {
        log.info("Controller: Getting all my petitions");
        Page<Petition> petitionPage = petitionService.findAllMy(name, status, page, size, auth);
        PaginationListResponse<PetitionFullResponse> response = new PaginationListResponse<>();
        response.setContent(petitionPage.getContent().stream().map(petitionMapper::fromPetitionToFullResponse).toList());
        response.setTotalPages(petitionPage.getTotalPages());
        return response;
    }

    @GetMapping("/created")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<PetitionFullResponse> getMyOwnPetitions(
            @PathVariable(name = "school_id") long schoolId,
            @PathParam("page") int page,
            @PathParam("size") int size,
            @PathParam("name") String name,
            @PathParam("status") String status,
            Authentication auth) {
        log.info("Controller: Getting all my created petitions");
        Page<Petition> petitionPage = petitionService.findAllByCreator(name, status, page, size, auth);
        PaginationListResponse<PetitionFullResponse> response = new PaginationListResponse<>();
        response.setContent(petitionPage.getContent().stream().map(petitionMapper::fromPetitionToFullResponse).toList());
        response.setTotalPages(petitionPage.getTotalPages());
        return response;
    }

    @GetMapping("/director")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<PetitionFullResponse> getPetitionsForDirector(
            @PathVariable(name = "school_id") long schoolId,
            @PathParam("page") int page,
            @PathParam("size") int size,
            @PathParam("name") String name,
            @PathParam("status") String status,
            Authentication auth) {
        log.info("Controller: Getting all petitions for director");
        Page<Petition> petitionPage = petitionService.findAllForDirector(name, status, page, size, auth);
        PaginationListResponse<PetitionFullResponse> response = new PaginationListResponse<>();
        response.setContent(petitionPage.getContent().stream().map(petitionMapper::fromPetitionToFullResponse).toList());
        response.setTotalPages(petitionPage.getTotalPages());
        return response;
    }

    @PostMapping("/support/{petition_id}")
    @ResponseStatus(HttpStatus.OK)
    public void supportPetition(@PathVariable(name = "school_id") long schoolId,
                                @PathVariable(name = "petition_id") long petitionId,
                                Authentication auth) {
        log.info("Controller: Support petition with id {}", petitionId);
        petitionService.support(petitionId, auth);
    }

    @PostMapping("/approve/{petition_id}")
    @ResponseStatus(HttpStatus.OK)
    public void approvePetition(@PathVariable(name = "school_id") long schoolId,
                                @PathVariable(name = "petition_id") long petitionId,
                                Authentication auth) {
        log.info("Controller: Approve petition with id {}", petitionId);
        petitionService.approve(petitionId);
    }

    @PostMapping("/reject/{petition_id}")
    @ResponseStatus(HttpStatus.OK)
    public void rejectPetition(@PathVariable(name = "school_id") long schoolId,
                               @PathVariable(name = "petition_id") long petitionId,
                               Authentication auth) {
        log.info("Controller: Reject petition with id {}", petitionId);
        petitionService.reject(petitionId);
    }

    @PostMapping("/{petition_id}/comments/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@PathVariable(name = "school_id") long schoolId,
                                         @PathVariable(name = "petition_id") long petitionId,
                                         @RequestBody CommentRequest commentRequest,
                                         Authentication auth) {
        log.info("Controller: Creating a new comment for petition with id {}", petitionId);
        return commentMapper.fromCommentToResponse(
                commentService.create(commentMapper.fromRequestToComment(commentRequest), auth, petitionId));
    }

    @PostMapping("/{petition_id}/comments/update/{comment_id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponse updateComment(@PathVariable(name = "school_id") long schoolId,
                                         @PathVariable(name = "petition_id") long petitionId,
                                         @PathVariable(name = "comment_id") long commentId,
                                         @RequestBody CommentRequest commentRequest,
                                         Authentication auth) {
        log.info("Controller: Updating a new comment for petition with id {}", petitionId);
        return commentMapper.fromCommentToResponse(
                commentService.update(commentMapper.fromRequestToComment(commentRequest), commentId));
    }

    @PostMapping("/{petition_id}/comments/delete/{comment_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(name = "school_id") long schoolId,
                              @PathVariable(name = "petition_id") long petitionId,
                              @PathVariable(name = "comment_id") long commentId,
                              @RequestBody CommentRequest commentRequest,
                              Authentication auth) {
        log.info("Controller: Deleting a new comment for petition with id {}", petitionId);
        commentService.delete(commentId);
    }

    @GetMapping("/{petition_id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<CommentResponse> getAllByPetition(
            @PathVariable(name = "school_id") long schoolId,
            @PathVariable(name = "petition_id") long petitionId,
            @PathParam("page") int page,
            @PathParam("size") int size){
        log.info("Controller: Getting all the comments for petition with id {}", petitionId);
        Page<Comment> commentPage = commentService.findAllByPetition(petitionId, page, size);
        PaginationListResponse<CommentResponse> response = new PaginationListResponse<>();
        response.setContent(commentPage.getContent().stream().map(commentMapper::fromCommentToResponse).toList());
        response.setTotalPages(commentPage.getTotalPages());
        return response;
    }
}

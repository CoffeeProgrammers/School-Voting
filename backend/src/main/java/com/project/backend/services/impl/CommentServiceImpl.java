package com.project.backend.services.impl;

import com.project.backend.models.User;
import com.project.backend.models.petitions.Comment;
import com.project.backend.repositories.petitions.CommentRepository;
import com.project.backend.services.inter.CommentService;
import com.project.backend.services.inter.PetitionService;
import com.project.backend.services.inter.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PetitionService petitionService;


    @Override
    public Comment create(Comment comment, User creator, long petitionId) {
        log.info("Service: Creating comment {}", comment);
        comment.setPetition(petitionService.findById(petitionId));
        comment.setCreator(creator);
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment, long id) {
        log.info("Service: Updating comment {}", id);
        Comment commentToUpdate = findById(id);
        commentToUpdate.setText(comment.getText());
        return commentRepository.save(commentToUpdate);
    }

    @Override
    public void delete(long id) {
        log.info("Service: Deleting comment {}", id);
        commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> findAllByPetition(long petitionId, int page, int size) {
        log.info("Service: Finding all comments by petition {}", petitionId);
        return commentRepository.findAllByPetition_Id(
                petitionId, PageRequest.of(
                        page, size, Sort.by(
                                Sort.Direction.DESC, "createdTime"
                        )
                )
        );
    }

    @Override
    public Comment findById(long id) {
        log.info("Service: Finding comment with id {}", id);
        return commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));
    }
}

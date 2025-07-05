package com.project.backend.services.impl.petition;

import com.project.backend.models.User;
import com.project.backend.models.petitions.Comment;
import com.project.backend.models.petitions.Petition;
import com.project.backend.repositories.repos.petitions.CommentRepository;
import com.project.backend.services.inter.petition.CommentService;
import com.project.backend.services.inter.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;


    @Override
    public Comment create(Comment comment, User creator, Petition petition) {
        log.info("Service: Creating comment {}", comment);
        comment.setPetition(petition);
        comment.setCreator(creator);
        comment.setCreatedTime(LocalDateTime.now());
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

    @Transactional
    @Override
    public void deleteWithPetition(long petitionId){
        log.info("Service: Deleting comments in petition {}", petitionId);
        commentRepository.deleteAllByPetition_Id(petitionId);
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

    @Override
    public void deleteingUser(long userId){
        log.info("Service: Deleting user {}", userId);
        commentRepository.saveAll(commentRepository.findAllByCreator_Id(userId).stream()
                .peek(comment -> comment.setCreator(userService.findUserByEmail("!deleted-user!@deleted.com"))).toList());
    }
}

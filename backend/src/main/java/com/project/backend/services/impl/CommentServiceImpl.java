package com.project.backend.services.impl;

import com.project.backend.models.petitions.Comment;
import com.project.backend.repositories.petitions.CommentRepository;
import com.project.backend.services.inter.CommentService;
import com.project.backend.services.inter.PetitionService;
import com.project.backend.services.inter.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PetitionService petitionService;


    @Override
    public Comment create(Comment comment, Authentication authentication, long petitionId) {
        comment.setPetition(petitionService.findById(petitionId));
        comment.setCreator(userService.findUserByKeycloakUserId(authentication.getName()));
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment, long id) {
        comment.setId(id);
        return commentRepository.save(comment);
    }

    @Override
    public void delete(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> findAllByPetition(long petitionId, int page, int size) {
        return commentRepository.findAllByPetition_Id(
                petitionId, PageRequest.of(
                        page, size, Sort.by(
                                Sort.Direction.DESC, "createdTime"
                        )
                )
        );
    }
}

package com.project.backend.services.impl;

import com.project.backend.models.petitions.Comment;
import com.project.backend.repositories.petitions.CommentRepository;
import com.project.backend.services.inter.CommentService;
import com.project.backend.services.inter.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;


    @Override
    public Comment create(Comment commentRequest, Authentication authentication, long eventId) {
        return null;
    }

    @Override
    public Comment update(Comment commentRequest, long id) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Page<Comment> getAllByPetition(long petitionId, long page, long size) {
        return null;
    }
}

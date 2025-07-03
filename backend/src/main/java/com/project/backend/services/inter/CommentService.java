package com.project.backend.services.inter;

import com.project.backend.models.petitions.Comment;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface CommentService {
    Comment create(Comment comment, Authentication authentication,
                   long petitionId);
    Comment update(Comment comment, long id);
    void delete (long id);
    Page<Comment> findAllByPetition(long petitionId, int page, int size);

    Comment findById(long id);
}

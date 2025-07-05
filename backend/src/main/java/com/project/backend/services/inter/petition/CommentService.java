package com.project.backend.services.inter.petition;

import com.project.backend.models.User;
import com.project.backend.models.petitions.Comment;
import com.project.backend.models.petitions.Petition;
import org.springframework.data.domain.Page;

public interface CommentService {
    Comment create(Comment comment, User creator, Petition petition);

    Comment update(Comment comment, long id);
    void delete (long id);

    void deleteWithPetition(long petitionId);

    Page<Comment> findAllByPetition(long petitionId, int page, int size);

    Comment findById(long id);

    void deleteingUser(long userId);
}

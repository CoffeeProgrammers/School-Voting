package com.project.backend.repositories;

import com.project.backend.models.petitions.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCreator_Id(Long creatorId);

    Long countAllByCreator_Id(Long creatorId);

}

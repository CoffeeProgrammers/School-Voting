package com.project.backend.mappers.petition;


import com.project.backend.dto.comment.CommentRequest;
import com.project.backend.dto.comment.CommentResponse;
import com.project.backend.models.petitions.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment fromRequestToComment(CommentRequest commentRequest);
    CommentResponse fromCommentToResponse(Comment comment);
}

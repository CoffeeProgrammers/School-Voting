package com.project.backend.mappers.voting;


import com.project.backend.dto.comment.CommentRequest;
import com.project.backend.dto.comment.CommentResponse;
import com.project.backend.models.petition.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment fromRequestToComment(CommentRequest commentRequest);
    CommentResponse fromCommentToResponse(Comment comment);
}

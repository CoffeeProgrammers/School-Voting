package com.project.backend.mappers.petition;


import com.project.backend.dto.answer.AnswerResponse;
import com.project.backend.models.voting.Answer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    AnswerResponse fromAnswerToResponse(Answer answer);
}

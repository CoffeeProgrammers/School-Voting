package com.project.backend.dto.voting;

import com.project.backend.dto.answer.AnswerResponse;
import lombok.Data;

import java.util.List;

@Data
public class VotingStatisticsResponse {
    private List<AnswerResponse> answers;
    private Long countAll;
    private Long countAnswered;
}

package com.project.backend.dto.voting;

import lombok.Data;

@Data
public class VotingListResponse {
    private Long id;
    private String name;
    private String levelType;
    private String startTime;
    private String endTime;
    private VotingStatisticsResponse statistics;
    private Boolean isAnswered;
}

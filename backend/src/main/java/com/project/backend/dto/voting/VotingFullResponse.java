package com.project.backend.dto.voting;

import com.project.backend.dto.user.UserListResponse;
import lombok.Data;

@Data
public class VotingFullResponse {
    private Long id;
    private String name;
    private String description;
    private String levelType;
    private String startTime;
    private String endTime;
    private UserListResponse creator;
    private VotingStatisticsResponse statistics;
    private Boolean isAnswered;
}

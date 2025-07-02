package com.project.backend.dto.voting;

import lombok.Data;

import java.util.List;

@Data
public class VotingCreateRequest {
    private String name;
    private String description;
    private String levelType;
    private String startTime;
    private String endTime;
    private List<String> answers;
}

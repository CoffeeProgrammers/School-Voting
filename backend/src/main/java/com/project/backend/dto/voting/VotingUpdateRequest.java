package com.project.backend.dto.voting;

import lombok.Data;

import java.util.List;

@Data
public class VotingUpdateRequest {
    private String name;
    private String description;
    private List<String> answers;
}

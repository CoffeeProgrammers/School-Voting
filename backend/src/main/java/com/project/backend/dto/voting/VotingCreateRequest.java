package com.project.backend.dto.voting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class VotingCreateRequest {
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    @NotBlank(message = "Name must be provided")
    private String name;
    @Size(min = 3, max = 999, message = "Description must be between 3 and 255 characters")
    private String description;
    @NotBlank(message = "Level Type must be provided")
    private String levelType;
    @NotBlank(message = "Start time must be provided")
    private String startTime;
    @NotBlank(message = "End time must be provided")
    private String endTime;
    @NotNull
    private List<String> answers;
    @NotNull
    private List<Long> targetIds;
}

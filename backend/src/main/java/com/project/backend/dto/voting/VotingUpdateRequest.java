package com.project.backend.dto.voting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class VotingUpdateRequest {
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;
    @Size(min = 3, max = 999, message = "Description must be between 3 and 255 characters")
    private String description;
    private List<String> answers;
}

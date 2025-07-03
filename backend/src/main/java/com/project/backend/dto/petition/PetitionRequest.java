package com.project.backend.dto.petition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PetitionRequest {
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    @NotBlank(message = "Name must be provided")
    private String name;
    @Size(min = 3, max = 999, message = "Description must be between 3 and 255 characters")
    private String description;
    @NotBlank(message = "Level Type must be provided")
    private String levelType;
    @NotBlank(message = "Object must be provided")
    private Long levelId;
}

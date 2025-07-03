package com.project.backend.dto.classDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class ClassCreateRequest {
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    @NotBlank(message = "Name must be provided")
    private String name;
    private List<Long> userIds;
}

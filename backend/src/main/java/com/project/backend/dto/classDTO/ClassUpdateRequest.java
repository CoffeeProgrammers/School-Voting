package com.project.backend.dto.classDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ClassUpdateRequest {
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;
}

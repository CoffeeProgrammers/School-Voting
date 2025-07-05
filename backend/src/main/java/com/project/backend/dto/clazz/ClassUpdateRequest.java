package com.project.backend.dto.clazz;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClassUpdateRequest {
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;
}

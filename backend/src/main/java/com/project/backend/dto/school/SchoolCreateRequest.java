package com.project.backend.dto.school;

import com.project.backend.dto.user.DirectorCreateRequest;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class SchoolCreateRequest {
    private String name;
    @Valid
    private DirectorCreateRequest director;
}

package com.project.backend.dto.school;

import com.project.backend.dto.user.DirectorCreateRequest;
import lombok.Data;

@Data
public class SchoolRequest {
    private String name;
    private DirectorCreateRequest director;
}

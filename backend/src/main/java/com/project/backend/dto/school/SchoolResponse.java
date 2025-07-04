package com.project.backend.dto.school;

import com.project.backend.dto.user.DirectorCreateRequest;
import com.project.backend.dto.user.UserFullResponse;
import lombok.Data;

@Data
public class SchoolResponse {
    private String name;
    private UserFullResponse director;
}

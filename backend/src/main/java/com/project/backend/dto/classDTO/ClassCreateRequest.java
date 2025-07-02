package com.project.backend.dto.classDTO;

import lombok.Data;

import java.util.List;

@Data
public class ClassCreateRequest {
    private String name;
    private List<Long> userIds;
}

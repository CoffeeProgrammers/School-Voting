package com.project.backend.dto.petition;

import lombok.Data;

@Data
public class PetitionRequest {
    private String name;
    private String description;
    private String levelType;
    private long levelId;
}

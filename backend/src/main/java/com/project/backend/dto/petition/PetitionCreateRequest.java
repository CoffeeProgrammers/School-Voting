package com.project.backend.dto.petition;

import com.project.backend.dto.user.UserListResponse;
import lombok.Data;

@Data
public class PetitionCreateRequest {
    private String name;
    private String description;
    private String endTime;
    private String levelType;
}

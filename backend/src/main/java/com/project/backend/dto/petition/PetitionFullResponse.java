package com.project.backend.dto.petition;

import com.project.backend.dto.user.UserListResponse;
import lombok.Data;

@Data
public class PetitionFullResponse {
    private Long id;
    private String name;
    private String description;
    private String endTime;
    private String levelType;
    private UserListResponse creator;
    private String status;
    private Long countSupported;
    private Long countNeeded;
    private Boolean supportedByCurrentId;
}

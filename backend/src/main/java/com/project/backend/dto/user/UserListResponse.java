package com.project.backend.dto.user;

import lombok.Data;

@Data
public class UserListResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}

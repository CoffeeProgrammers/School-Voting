package com.project.backend.dto.user;

import lombok.Data;

@Data
public class UserFullResponse {
    private Long id;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
}

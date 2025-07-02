package com.project.backend.dto.classDTO;

import com.project.backend.dto.user.UserListResponse;
import lombok.Data;

import java.util.List;

@Data
public class ClassFullResponse {
    private Long id;
    private String name;
    private List<UserListResponse> users;
}

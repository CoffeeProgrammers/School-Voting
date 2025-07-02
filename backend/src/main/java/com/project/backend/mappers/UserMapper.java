package com.project.backend.mappers;

import com.project.backend.dto.user.UserCreateRequest;
import com.project.backend.dto.user.UserFullResponse;
import com.project.backend.dto.user.UserListResponse;
import com.project.backend.dto.user.UserUpdateRequest;
import com.project.backend.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserFullResponse fromUserToFullResponse(User user);

    UserListResponse fromUserToListResponse(User user);

    User fromRequestToUser(UserCreateRequest userCreateRequest);
    User fromRequestToUser(UserUpdateRequest userUpdateRequest);
}

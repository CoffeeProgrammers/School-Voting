package com.project.backend.mappers;

import com.project.backend.dto.user.UserCreateRequest;
import com.project.backend.dto.user.UserFullResponse;
import com.project.backend.dto.user.UserListResponse;
import com.project.backend.dto.user.UserUpdateRequest;
import com.project.backend.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserFullResponse fromUserToUserResponse(User user);

    UserListResponse fromUserToUserListResponse(User user);

    User fromUserRequestToUser(UserCreateRequest userCreateRequest);
    User fromUserRequestToUser(UserUpdateRequest userUpdateRequest);
}

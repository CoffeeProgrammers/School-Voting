package com.project.backend.mappers;

import com.project.backend.dto.user.*;
import com.project.backend.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserFullResponse fromUserToFullResponse(User user);

    UserListResponse fromUserToListResponse(User user);

    User fromRequestToUser(UserCreateRequest userCreateRequest);
    User fromRequestToUser(UserUpdateRequest userUpdateRequest);

    User fromDirectorRequestToUser(DirectorCreateRequest directorCreateRequest);
}

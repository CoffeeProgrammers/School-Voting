package com.project.backend.mappers;

import com.project.backend.dto.clazz.ClassCreateRequest;
import com.project.backend.dto.clazz.ClassFullResponse;
import com.project.backend.dto.clazz.ClassListResponse;
import com.project.backend.dto.clazz.ClassUpdateRequest;
import com.project.backend.models.Class;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    Class fromRequestToClass(ClassCreateRequest classCreateRequest);
    Class fromRequestToClass(ClassUpdateRequest classUpdateRequest);
    ClassListResponse fromClassToListResponse(Class classObj);
    ClassFullResponse fromClassToFullResponse(Class classObj);
}

package com.project.backend.mappers;

import com.project.backend.dto.clazz.ClassCreateRequest;
import com.project.backend.dto.clazz.ClassResponse;
import com.project.backend.dto.clazz.ClassUpdateRequest;
import com.project.backend.models.Class;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    Class fromRequestToClass(ClassCreateRequest classCreateRequest);
    Class fromRequestToClass(ClassUpdateRequest classUpdateRequest);
    ClassResponse fromClassToResponse(Class classObj);
}

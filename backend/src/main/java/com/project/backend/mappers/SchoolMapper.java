package com.project.backend.mappers;

import com.project.backend.dto.school.SchoolRequest;
import com.project.backend.models.School;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SchoolMapper {
    School fromRequestToSchool(SchoolRequest schoolRequest);
}

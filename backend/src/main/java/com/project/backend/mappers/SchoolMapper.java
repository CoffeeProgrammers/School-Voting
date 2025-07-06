package com.project.backend.mappers;

import com.project.backend.dto.school.SchoolCreateRequest;
import com.project.backend.dto.school.SchoolResponse;
import com.project.backend.dto.school.SchoolUpdateRequest;
import com.project.backend.models.School;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SchoolMapper {
    School fromRequestToSchool(SchoolCreateRequest schoolRequest);
    SchoolResponse fromSchoolToResponse(School school);
    School fromRequestToSchool(SchoolUpdateRequest schoolUpdateRequest);
}

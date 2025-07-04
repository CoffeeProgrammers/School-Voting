package com.project.backend.mappers;

import com.project.backend.dto.school.SchoolRequest;
import com.project.backend.dto.school.SchoolResponse;
import com.project.backend.models.School;
import com.project.backend.services.inter.SchoolService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SchoolMapper {
    School fromRequestToSchool(SchoolRequest schoolRequest);
    SchoolResponse fromSchoolToResponse(School school);
}

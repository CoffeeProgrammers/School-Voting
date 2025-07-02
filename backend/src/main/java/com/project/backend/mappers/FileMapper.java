package com.project.backend.mappers;

import com.project.backend.dto.file.FileSimpleResponse;
import com.project.backend.models.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileSimpleResponse toSimpleResponse(File file);
}

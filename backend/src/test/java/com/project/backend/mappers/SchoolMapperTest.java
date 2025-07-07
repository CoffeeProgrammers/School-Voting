package com.project.backend.mappers;

import com.project.backend.TestUtil;
import com.project.backend.dto.school.SchoolCreateRequest;
import com.project.backend.dto.school.SchoolResponse;
import com.project.backend.dto.school.SchoolUpdateRequest;
import com.project.backend.models.School;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class SchoolMapperTest {

    private final SchoolMapper schoolMapper = Mappers.getMapper(SchoolMapper.class);

    private School school;

    @BeforeEach
    void setUp() {
        school = TestUtil.createSchool("SCHOOL");
    }


    @Test
    void fromSchoolToResponse_null() {
        SchoolResponse dto = schoolMapper.fromSchoolToResponse(null);
        assertNull(dto);
    }

    @Test
    void fromSchoolToResponse_success() {
        SchoolResponse dto = schoolMapper.fromSchoolToResponse(school);

        assertNotNull(dto);
        assertEquals(school.getName(), dto.getName());
        assertEquals(school.getDirector().getEmail(), dto.getDirector().getEmail());
    }

    @Test
    void fromSchoolToResponse_directorIsNull() {
        school.setDirector(null);
        SchoolResponse dto = schoolMapper.fromSchoolToResponse(school);

        assertNotNull(dto);
        assertEquals(school.getName(), dto.getName());
        assertNull(school.getDirector());
    }

    @Test
    void fromCreateRequestToSchool_null() {
        SchoolCreateRequest dto = null;
        School school1 = schoolMapper.fromRequestToSchool(dto);
        assertNull(school1);
    }

    @Test
    void fromCreateRequestToSchool_success() {
        SchoolCreateRequest dto = Instancio.create(SchoolCreateRequest.class);
        School school1 = schoolMapper.fromRequestToSchool(dto);

        assertNotNull(school1);
        assertEquals(dto.getName(), school1.getName());
    }

    @Test
    void fromCreateRequestToSchool_directorIsNull() {
        SchoolCreateRequest dto = Instancio.create(SchoolCreateRequest.class);
        dto.setDirector(null);
        School school1 = schoolMapper.fromRequestToSchool(dto);

        assertNotNull(school1);
        assertNull(school1.getDirector());
        assertEquals(dto.getName(), school1.getName());
    }

    @Test
    void fromUpdateRequestToSchool_null() {
        SchoolUpdateRequest dto = null;
        School school1 = schoolMapper.fromRequestToSchool(dto);
        assertNull(school1);
    }

    @Test
    void fromUpdateRequestToSchool_success() {
        SchoolUpdateRequest dto = Instancio.create(SchoolUpdateRequest.class);
        School school1 = schoolMapper.fromRequestToSchool(dto);

        assertNotNull(school1);
        assertEquals(dto.getName(), school1.getName());
    }
}

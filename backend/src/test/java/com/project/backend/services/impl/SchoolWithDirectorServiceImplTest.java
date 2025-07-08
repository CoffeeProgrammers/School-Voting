package com.project.backend.services.impl;

import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.services.inter.SchoolService;
import com.project.backend.services.inter.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SchoolWithDirectorServiceImplTest {

    @Mock
    private SchoolService schoolService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SchoolWithDirectorServiceImpl schoolWithDirectorService;

    private School school;
    private User director;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        school = new School();
        school.setId(1L);
        school.setName("Test School");

        director = new User();
        director.setId(1L);
        director.setEmail("director@example.com");
    }

    @Test
    void createSchoolWithDirector_shouldCreateCorrectly() {
        String password = "securePass";

        // Мок дії
        when(schoolService.save(any(School.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userService.createDirector(any(User.class), eq(password)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(2L);
                    return u;
                });

        School result = schoolWithDirectorService.createSchoolWithDirector(school, director, password);

        assertNotNull(result);
        assertEquals("Test School", result.getName());
        assertNotNull(result.getDirector());
        assertEquals("director@example.com", result.getDirector().getEmail());
        assertEquals("DIRECTOR", result.getDirector().getRole());
        assertEquals(result, result.getDirector().getSchool());

        verify(userService).checkEmail("director@example.com");
        verify(schoolService, times(2)).save(any(School.class)); // 1 для школи, 1 для оновлення з директором
        verify(userService).createDirector(any(User.class), eq(password));
    }
}

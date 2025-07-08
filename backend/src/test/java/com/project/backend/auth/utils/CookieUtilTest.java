package com.project.backend.auth.utils;

import com.project.backend.models.School;
import com.project.backend.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilTest {

    @Test
    void testDeleteCookie() {
        ResponseCookie cookie = CookieUtil.deleteCookie("testCookie");

        assertEquals("testCookie", cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge().getSeconds());
        assertEquals("/", cookie.getPath());
    }

    @Test
    void testCreateCookieWithMaxAge() {
        ResponseCookie cookie = CookieUtil.createCookie("token", "abc123", 3600, true);

        assertEquals("token", cookie.getName());
        assertEquals("abc123", cookie.getValue());
        assertEquals(3600, cookie.getMaxAge().getSeconds());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }

    @Test
    void testCreateCookieWithoutMaxAge() {
        ResponseCookie cookie = CookieUtil.createCookie("session", "xyz", -1, false);

        assertEquals("session", cookie.getName());
        assertEquals("xyz", cookie.getValue());
        assertEquals(Duration.of(-1, ChronoUnit.SECONDS), cookie.getMaxAge());
        assertFalse(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }

    @Test
    void testCreateCookiesFromJWTs() {
        // mock user
        User user = new User();
        user.setId(42L);
        School school = new School();
        school.setId(100L);
        user.setSchool(school);

        String accessToken = "access";
        String refreshToken = "refresh";
        String role = "ADMIN";
        Long expiresIn = 3600L;
        Long refreshExpiresIn = 86400L;

        List<String> cookies = CookieUtil.createCookiesFromJWTs(
                user, accessToken, refreshToken, role, expiresIn, refreshExpiresIn
        );

        assertEquals(5, cookies.size());
        assertTrue(cookies.stream().anyMatch(s -> s.contains("accessToken=access")));
        assertTrue(cookies.stream().anyMatch(s -> s.contains("refreshToken=refresh")));
        assertTrue(cookies.stream().anyMatch(s -> s.contains("userId=42")));
        assertTrue(cookies.stream().anyMatch(s -> s.contains("schoolId=100")));
        assertTrue(cookies.stream().anyMatch(s -> s.contains("role=ADMIN")));
    }

    @Test
    void testConstructor() {
        CookieUtil cookieUtil = new CookieUtil();
        assertEquals(CookieUtil.class, cookieUtil.getClass());
    }
}

package com.project.backend.auth.utils;

import com.project.backend.models.User;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CookieUtil {
    public static String getCookie(Cookie[] cookies, String name) {
        if(cookies != null) {
            log.info("COOKIES: {}", Arrays.stream(cookies).map(cookie -> cookie.getName() + ":" + cookie.getValue()).collect(Collectors.joining(", ")));
        } else {
            log.info("COOKIES: NULL!");
        }
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .path("/")
                .maxAge(0)
                .build();
    }

    public static ResponseCookie createCookie(String name, String value, long maxAge, boolean httpOnly) {
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(name, value)
                .httpOnly(httpOnly)
                .path("/");

        if (maxAge != -1) {
            responseCookieBuilder.maxAge(Duration.ofSeconds(maxAge));
        }

        return responseCookieBuilder.build();
    }

    public static List<String> createCookiesFromJWTs(User user, String accessTokenString, String refreshTokenString, String role, Long expiresIn, Long refreshExpiresIn) {
        ResponseCookie accessTokenCookie = createCookie("accessToken", accessTokenString, expiresIn, false);
        ResponseCookie refreshTokenCookie = createCookie("refreshToken", refreshTokenString, refreshExpiresIn, false);
        ResponseCookie userIdCookie = createCookie("userId", String.valueOf(user.getId()), -1, false);
        ResponseCookie schoolIdCookie = createCookie("schoolId", String.valueOf(user.getSchool().getId()), -1, false);
        ResponseCookie roleCookie = createCookie("role", role, -1, false);
        return List.of(
                accessTokenCookie.toString(),
                userIdCookie.toString(),
                schoolIdCookie.toString(),
                roleCookie.toString(),
                refreshTokenCookie.toString()
        );
    }
}

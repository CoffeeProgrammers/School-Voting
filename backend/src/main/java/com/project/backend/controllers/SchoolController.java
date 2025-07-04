package com.project.backend.controllers;

import com.project.backend.auth.utils.CookieUtil;
import com.project.backend.auth.utils.SecurityUtil;
import com.project.backend.dto.school.SchoolRequest;
import com.project.backend.mappers.SchoolMapper;
import com.project.backend.mappers.UserMapper;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.services.inter.SchoolWithDirectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolWithDirectorService schoolWithDirectorService;
    private final SchoolMapper schoolMapper;
    private final UserMapper userMapper;
    private final JwtDecoder jwtDecoder;

    @Value("${client-id}")
    private String clientId;

    @Value("${client-secret}")
    private String clientSecret;

    @Value("${realm}")
    private String realm;


    private final WebClient webClient;

    @PostMapping("/create")
    public ResponseEntity<String> createSchool(@RequestBody SchoolRequest schoolRequest) {
        School school = schoolMapper.fromRequestToSchool(schoolRequest);
        User director = userMapper.fromDirectorRequestToUser(schoolRequest.getDirector());
        try {
            school = schoolWithDirectorService.createSchoolWithDirector(school, director, schoolRequest.getDirector().getPassword());
        } catch (WebClientResponseException e) {
            log.error("Controller: {} Error response from Keycloak: {}", e.getRawStatusCode(), e.getResponseBodyAsString());

            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }director = school.getDirector();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", director.getEmail());
        formData.add("password", schoolRequest.getDirector().getPassword());

        Map response;
        try {
            response = SecurityUtil.sendRequestToTokenEndpoint(webClient, realm, formData);
        } catch (WebClientResponseException e) {
            log.error("Controller: {} Error response from Keycloak: {}", e.getRawStatusCode(), e.getResponseBodyAsString());

            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
        String accessTokenString = (String) response.get("access_token");
        String refreshTokenString = (String) response.get("refresh_token");
        List<String> cookies = CookieUtil.createCookiesFromJWTs(
                school.getDirector(),
                accessTokenString,
                refreshTokenString,
                SecurityUtil.extractRole(clientId, jwtDecoder.decode(accessTokenString)),
                Long.parseLong(response.get("expires_in").toString()),
                Long.parseLong(response.get("refresh_expires_in").toString()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders -> httpHeaders.put(HttpHeaders.SET_COOKIE, cookies))
                .build();
    }
}

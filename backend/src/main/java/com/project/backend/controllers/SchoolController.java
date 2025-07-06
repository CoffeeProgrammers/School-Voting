package com.project.backend.controllers;

import com.project.backend.auth.utils.CookieUtil;
import com.project.backend.auth.utils.SecurityUtil;
import com.project.backend.dto.school.SchoolCreateRequest;
import com.project.backend.dto.school.SchoolResponse;
import com.project.backend.dto.school.SchoolUpdateRequest;
import com.project.backend.mappers.SchoolMapper;
import com.project.backend.mappers.UserMapper;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.services.inter.*;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
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
    private final PetitionService petitionService;
    private final VotingService votingService;
    private final SchoolService schoolService;
    private final ClassService classService;
    private final SchoolMapper schoolMapper;
    private final UserMapper userMapper;
    private final JwtDecoder jwtDecoder;
    private final UserService userService;
    private final UserDeletionService userDeletionService;

    @Value("${client-id}")
    private String clientId;

    @Value("${client-secret}")
    private String clientSecret;

    @Value("${realm}")
    private String realm;


    private final WebClient webClient;

    @PostMapping("/create")
    public ResponseEntity<String> createSchool(@Valid @RequestBody SchoolCreateRequest schoolCreateRequest) {
        School school = schoolMapper.fromRequestToSchool(schoolCreateRequest);
        User director = userMapper.fromDirectorRequestToUser(schoolCreateRequest.getDirector());
        try {
            school = schoolWithDirectorService.createSchoolWithDirector(school, director, schoolCreateRequest.getDirector().getPassword());
        } catch (WebClientResponseException e) {
            log.error("Controller: {} Error response from Keycloak: {}", e.getRawStatusCode(), e.getResponseBodyAsString());

            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
        director = school.getDirector();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", director.getEmail());
        formData.add("password", schoolCreateRequest.getDirector().getPassword());

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

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId)")
    @GetMapping("/{school_id}")
    @ResponseStatus(HttpStatus.OK)
    public SchoolResponse getSchoolById(@PathVariable("school_id") long schoolId, Authentication auth) {
        return schoolMapper.fromSchoolToResponse(schoolService.findById(schoolId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('DIRECTOR')")
    @PutMapping("/update/{school_id}")
    @ResponseStatus(HttpStatus.OK)
    public SchoolResponse updateSchool(@PathVariable("school_id") long schoolId, @RequestBody SchoolUpdateRequest schoolUpdateRequest, Authentication auth) {
        return schoolMapper.fromSchoolToResponse(schoolService.update(schoolMapper.fromRequestToSchool(schoolUpdateRequest), schoolId));
    }

    @Transactional
    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasRole('DIRECTOR')")
    @DeleteMapping("/delete/{school_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSchool(@PathVariable("school_id") long schoolId, Authentication auth) {
        petitionService.deleteBy(LevelType.SCHOOL, schoolId);
        votingService.deleteBy(LevelType.SCHOOL, schoolId);
        classService.deleteBySchool(schoolId);
        userService.findAllBySchool(schoolId).forEach(user -> {userDeletionService.delete(user, true);});
        schoolService.delete(schoolId);
    }
}

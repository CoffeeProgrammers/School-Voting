package com.project.backend.controllers;

import com.project.backend.dto.clazz.ClassCreateRequest;
import com.project.backend.dto.clazz.ClassResponse;
import com.project.backend.dto.clazz.ClassUpdateRequest;
import com.project.backend.dto.user.UserListResponse;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.mappers.ClassMapper;
import com.project.backend.mappers.UserMapper;
import com.project.backend.models.Class;
import com.project.backend.models.User;
import com.project.backend.models.petition.Petition;
import com.project.backend.services.inter.ClassService;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.google.GoogleCalendarCredentialService;
import com.project.backend.services.inter.google.GoogleCalendarService;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import com.project.backend.services.inter.voting.VotingUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/schools/{school_id}/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final ClassMapper classMapper;
    private final UserMapper userMapper;
    private final VotingUserService votingUserService;
    private final PetitionService petitionService;
    private final VotingService votingService;
    private final UserService userService;
    private final GoogleCalendarService googleCalendarService;
    private final GoogleCalendarCredentialService googleCalendarCredentialService;

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassResponse createClass(@PathVariable(value = "school_id") long schoolId,
                                         @Valid @RequestBody ClassCreateRequest classCreateRequest,
                                         Authentication auth) {
        log.info("Controller: Creating a new class");
        return classMapper.fromClassToResponse(
                classService.create(classMapper.fromRequestToClass(classCreateRequest), classCreateRequest.getUserIds(), schoolId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @PutMapping("/update/{class_id}")
    @ResponseStatus(HttpStatus.OK)
    public ClassResponse updateClass(@PathVariable(value = "school_id") long schoolId,
                                         @PathVariable(value = "class_id") long classId,
                                         @Valid @RequestBody ClassUpdateRequest classUpdateRequest,
                                         Authentication auth) {
        log.info("Controller: Updating class with id {}", classId);
        return classMapper.fromClassToResponse(
                classService.update(classMapper.fromRequestToClass(classUpdateRequest), classId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @DeleteMapping("/delete/{class_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClass(@PathVariable(value = "school_id") long schoolId,
                            @PathVariable(value = "class_id") long classId,
                            @RequestParam boolean deletedUsers,
                            Authentication auth) {
        log.info("Controller: Deleting class with id {}", classId);
        classService.delete(classId, deletedUsers);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping("/{class_id}")
    @ResponseStatus(HttpStatus.OK)
    public ClassResponse getClassById(@PathVariable(value = "school_id") long schoolId,
                                          @PathVariable(value = "class_id") long classId,
                                          Authentication auth) {
        log.info("Controller: Getting class with id {}", classId);
        return classMapper.fromClassToResponse(classService.findById(classId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('STUDENT')")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public ClassResponse getMyClass(@PathVariable(value = "school_id") long schoolId,
                                          Authentication auth) {
        log.info("Controller: Getting my class user");
        User user = userService.findUserByAuth(auth);
        Class clazz = classService.findByUser(user);
        return clazz != null ? classMapper.fromClassToResponse(clazz) : null;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<ClassResponse> getClasses(
            @PathVariable(value = "school_id") long schoolId,
            @RequestParam(required = false) String name,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth) {
        log.info("Controller: Getting all the classes in school with id {}", schoolId);
        PaginationListResponse<ClassResponse> paginationListResponse = new PaginationListResponse<>();
        Page<Class> classes = classService.findAllBySchool(schoolId, name, page, size);
        paginationListResponse.setContent(classes.stream().map(classMapper::fromClassToResponse).toList());
        paginationListResponse.setTotalPages(classes.getTotalPages());
        return paginationListResponse;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @PostMapping("/{class_id}/assign-users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserListResponse> assignUsers(@PathVariable(value = "school_id") long schoolId,
                                              @PathVariable(value = "class_id") long classId,
                                              @RequestBody List<Long> userIds,
                                              Authentication auth) {
        log.info("Controller: Assigning users");
        userIds = classService.assignUserToClass(classId, userIds);
        for(Long userId : userIds) {
            votingUserService.create(votingService.findAllByClass(classId), userService.findById(userId));
            if(googleCalendarCredentialService.existsByUserId(userId)) {
                googleCalendarService.saveAllClassPetitionsAndVotingsToUsers(userId);
            }
        }
        return userIds.stream().map(userId -> userMapper.fromUserToListResponse(userService.findById(userId))).collect(Collectors.toList());
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @PostMapping("/{class_id}/unassign-users")
    @ResponseStatus(HttpStatus.OK)
    public void unassignUsers(@PathVariable(value = "school_id") long schoolId,
                              @PathVariable(value = "class_id") long classId,
                              @RequestBody List<Long> userIds,
                              Authentication auth) {
        log.info("Controller: Unassigning users");
        for(Long userId : userIds) {
            if(userService.findById(userId).getMyClass().getId() != classId){
                log.warn("Service: Can`t unassign Users with id {} from Class with id {} because this user not in this class", userId, classId);
                continue;
            }
            if(googleCalendarCredentialService.existsByUserId(userId)) {
                googleCalendarService.deleteAllClassPetitionsAndVotingsFromUsers(userId);
            }
            votingUserService.deleteWithUser(userId);
            petitionService.deleteVoteByUser(userId);
        }
        classService.unassignUserFromClass(classId, userIds);
        List<Petition> petitions = petitionService.findAllByClass(classId);
        for(Petition petition : petitions) {
            petitionService.checkingStatus(petition);
            petitionService.save(petition);
        }
    }
}

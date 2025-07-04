package com.project.backend.controllers;

import com.project.backend.dto.classDTO.ClassCreateRequest;
import com.project.backend.dto.classDTO.ClassFullResponse;
import com.project.backend.dto.classDTO.ClassListResponse;
import com.project.backend.dto.classDTO.ClassUpdateRequest;
import com.project.backend.dto.wrapper.PaginationListResponse;
import com.project.backend.mappers.ClassMapper;
import com.project.backend.models.Class;
import com.project.backend.services.inter.ClassService;
import com.project.backend.services.inter.PetitionService;
import com.project.backend.services.inter.VotingUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schools/{school_id}/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final ClassMapper classMapper;
    private final VotingUserService votingUserService;
    private final PetitionService petitionService;

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassFullResponse createClass(@PathVariable(value = "school_id") long schoolId,
                                         @Valid @RequestBody ClassCreateRequest classCreateRequest,
                                         Authentication auth) {
        log.info("Controller: Creating a new class");
        return classMapper.fromClassToFullResponse(
                classService.create(classMapper.fromRequestToClass(classCreateRequest), classCreateRequest.getUserIds(), schoolId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @PutMapping("/update/{class_id}")
    @ResponseStatus(HttpStatus.OK)
    public ClassFullResponse updateClass(@PathVariable(value = "school_id") long schoolId,
                                         @PathVariable(value = "class_id") long classId,
                                         @Valid @RequestBody ClassUpdateRequest classUpdateRequest,
                                         Authentication auth) {
        log.info("Controller: Updating class with id {}", classId);
        return classMapper.fromClassToFullResponse(
                classService.update(classMapper.fromRequestToClass(classUpdateRequest), classId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @DeleteMapping("/delete/{class_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClass(@PathVariable(value = "school_id") long schoolId,
                            @PathVariable(value = "class_id") long classId,
                            Authentication auth) {
        log.info("Controller: Deleting class with id {}", classId);
        classService.delete(classId);
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping("/{class_id}")
    @ResponseStatus(HttpStatus.OK)
    public ClassFullResponse getClassById(@PathVariable(value = "school_id") long schoolId,
                                          @PathVariable(value = "class_id") long classId,
                                          Authentication auth) {
        log.info("Controller: Getting class with id {}", classId);
        return classMapper.fromClassToFullResponse(classService.findById(classId));
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginationListResponse<ClassListResponse> getClasses(
            @PathVariable(value = "school_id") long schoolId,
            @RequestParam(required = false) String name,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth) {
        log.info("Controller: Getting all the classes in school with id {}", schoolId);
        PaginationListResponse<ClassListResponse> paginationListResponse = new PaginationListResponse<>();
        Page<Class> classes = classService.findAllBySchool(schoolId, name, page, size);
        paginationListResponse.setContent(classes.stream().map(classMapper::fromClassToListResponse).toList());
        paginationListResponse.setTotalPages(classes.getTotalPages());
        return paginationListResponse;
    }

    @PreAuthorize("@userSecurity.checkUserSchool(#auth, #schoolId) and hasAnyRole('DIRECTOR', 'TEACHER')")
    @PostMapping("/{class_id}/assign-users")
    @ResponseStatus(HttpStatus.OK)
    public void assignUsers(@PathVariable(value = "school_id") long schoolId,
                            @PathVariable(value = "class_id") long classId,
                            @RequestBody List<Long> userIds,
                            Authentication auth) {
        log.info("Controller: Assigning users");
        classService.assignUserToClass(classId, userIds);
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
            votingUserService.deleteWithUser(userId);
            petitionService.deleteVoteByUser(userId);
        }
        classService.unassignUserFromClass(classId, userIds);
    }

}

package com.project.backend.services.impl;

import com.project.backend.models.Class;
import com.project.backend.repositories.ClassRepository;
import com.project.backend.repositories.UserRepository;
import com.project.backend.repositories.specification.ClassSpecification;
import com.project.backend.repositories.specification.UserSpecification;
import com.project.backend.services.inter.ClassService;
import com.project.backend.services.inter.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static com.project.backend.utils.SpecificationUtil.isValid;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final UserService userService;

    @Override
    public Class create(Class classRequest) {
        log.info("Service: Creating Class");
        return classRepository.save(classRequest);
    }

    @Override
    public Class update(Class classRequest, long id) {
        log.info("Service: Updating Class with id {}", id);
        Class clazz = findById(id);
        clazz.setName(classRequest.getName());
        return classRepository.save(clazz);
    }

    @Override
    public void delete(long id) {
        log.info("Service: Deleting Class with id {}", id);
        findById(id);
        classRepository.deleteById(id);
    }

    @Override
    public Class findById(long id) {
        log.info("Service: Finding Class with id {}", id);
        return classRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Class not found with id " + id));
    }

    @Override
    public void assignUserToClass(long classId, List<Long> userIds) {
        log.info("Service: Assigning User with id {} to Class with id {}", userIds, classId);
        Class clazz = findById(classId);
        for(Long userId : userIds) {
            clazz.getUsers().add(userService.findById(userId));
        }
        classRepository.save(clazz);
    }

    @Override
    public void unassignUserFromClass(long classId, List<Long> userIds) {
        log.info("Service: Unassigning User with id {} from Class with id {}", userIds, classId);
        Class clazz = findById(classId);
        for(Long userId : userIds) {
            clazz.getUsers().remove(userService.findById(userId));
        }
        classRepository.save(clazz);
    }

    @Override
    public Page<Class> findAllBySchool(long schoolId, String name, int page, int size) {
        return classRepository.findAll(ClassSpecification.bySchool(schoolId)
                .and(ClassSpecification.byName(isValid(name) ? name : "")),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));
    }
}

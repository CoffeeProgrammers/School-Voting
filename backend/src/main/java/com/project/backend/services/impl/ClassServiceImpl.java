package com.project.backend.services.impl;

import com.project.backend.models.Class;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.repositories.ClassRepository;
import com.project.backend.repositories.specification.ClassSpecification;
import com.project.backend.services.inter.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.backend.utils.SpecificationUtil.isValid;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final UserService userService;
    private final SchoolService schoolService;
    private final PetitionService petitionService;
    private final VotingService votingService;

    @Override
    public Class create(Class classRequest, List<Long> userIds, long schoolId) {
        log.info("Service: Creating Class {} with users {} for school {}", classRequest.getName(), userIds, schoolId);
        classRequest.setSchool(schoolService.findById(schoolId));
        classRequest.setUsers(userIds.stream().map(userService::findById).collect(Collectors.toSet()));
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
    public void deleteBySchool(long schoolId){
        log.info("Service: Deleting Class with by school with id {}", schoolId);
        List<Class> classes = classRepository.findAll(ClassSpecification.bySchool(schoolId));
        for (Class clazz : classes) {
            delete(clazz.getId(), true);
        }
    }

    @Override
    public void delete(long id, boolean deleteUsers) {
        log.info("Service: Deleting Class with id {}", id);
        Class aClass = findById(id);
        votingService.deleteBy(LevelType.CLASS, id);
        petitionService.deleteBy(LevelType.CLASS, id);
        if(deleteUsers) {
            for (User user : aClass.getUsers()) {
                userService.delete(user.getId());
            }
        }
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
        User user;
        if (userIds.isEmpty()){
            return;
        }
        for(Long userId : userIds) {
            user = userService.findById(userId);
            if(user.getRole().equals("STUDENT")) {
                clazz.getUsers().add(user);
                userService.assignClassToUser(clazz, user);
            }
        }
        classRepository.save(clazz);
    }

    @Override
    public void unassignUserFromClass(long classId, List<Long> userIds) {
        log.info("Service: Unassigning User with id {} from Class with id {}", userIds, classId);
        Class clazz = findById(classId);
        User user;
        if (userIds.isEmpty()){
            return;
        }
        for(Long userId : userIds) {
            user = userService.findById(userId);
            if(user.getRole().equals("STUDENT")) {
                clazz.getUsers().remove(user);
                userService.unassignClassFromUser(user);
            }
        }
        classRepository.save(clazz);
    }

    @Override
    public Page<Class> findAllBySchool(long schoolId, String name, int page, int size) {
        log.info("Service: Finding all classes by school {} with name {}", schoolId, name);
        Specification<Class> specification = isValid(name) ?
                ClassSpecification.bySchool(schoolId).and(ClassSpecification.byName(name)) : ClassSpecification.bySchool(schoolId);
        return classRepository.findAll(specification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));
    }
}

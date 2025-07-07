package com.project.backend.services.impl;

import com.project.backend.models.Class;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.repositories.repos.ClassRepository;
import com.project.backend.repositories.specification.ClassSpecification;
import com.project.backend.services.inter.ClassService;
import com.project.backend.services.inter.SchoolService;
import com.project.backend.services.inter.UserDeletionService;
import com.project.backend.services.inter.UserService;
import com.project.backend.services.inter.petition.PetitionService;
import com.project.backend.services.inter.voting.VotingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.project.backend.utils.SpecificationUtil.isValid;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final UserDeletionService userDeletionService;
    private final ClassRepository classRepository;
    private final UserService userService;
    private final SchoolService schoolService;
    private final PetitionService petitionService;
    private final VotingService votingService;


    @Override
    public Class create(Class classRequest, List<Long> userIds, long schoolId) {
        log.info("Service: Creating Class {} with users {} for school {}", classRequest.getName(), userIds, schoolId);
        Set<User> users = userIds.stream().map(userService::findById).collect(Collectors.toSet());
        classRequest.setSchool(schoolService.findById(schoolId));
        classRequest.setUsers(users);
        Class clazz = classRepository.save(classRequest);
        users.stream().forEach(u -> {
            u.setMyClass(clazz);
            userService.save(u);
        });
        log.info("Service: Class created: {}", clazz);
        return clazz;
    }

    @Override
    public Class update(Class classRequest, long id) {
        log.info("Service: Updating Class with id {}", id);
        Class clazz = findById(id);
        clazz.setName(classRequest.getName());
        return classRepository.save(clazz);
    }

    @Override
    public void deleteBySchool(long schoolId) {
        log.info("Service: Deleting Class from school with id {}", schoolId);
        List<Class> classes = classRepository.findAll(ClassSpecification.bySchool(schoolId));
        for (Class clazz : classes) {
            delete(clazz.getId(), true);
        }
        log.info("Service: All classes from school with id {} deleted",  schoolId);
    }

    @Override
    public void delete(long id, boolean deleteUsers) {
        log.info("Service: Deleting Class with id {}", id);
        Class aClass = findById(id);
        votingService.deleteBy(LevelType.CLASS, id);
        petitionService.deleteBy(LevelType.CLASS, id);
        if (deleteUsers) {
            for (User user : aClass.getUsers()) {
                userDeletionService.delete(user, false);
            }
        }
        classRepository.deleteById(id);
        log.info("Service: Deleted Class with id {}", id);
    }

    @Override
    public Class findById(long id) {
        log.info("Service: Finding Class with id {}", id);
        return classRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Class not found with id " + id));
    }

    @Override
    public void assignUserToClass(long classId, List<Long> userIds) {
        log.info("Service: Assigning Users with id {} to Class with id {}", userIds, classId);
        Class clazz = findById(classId);
        User user;
        if (userIds.isEmpty()) {
            return;
        }
        for (Long userId : userIds) {
            user = userService.findById(userId);
            if(user.getMyClass() != null){
                log.warn("Service: Can`t assign Users with id {} to Class with id {}", userId, classId);
                continue;
            }
            if (user.getRole().equals("STUDENT")) {
                clazz.getUsers().add(user);
                userService.assignClassToUser(clazz, user);
            }
        }
        classRepository.save(clazz);
        log.info("Service: Assigned Users with id {} to Class with id {}", userIds, classId);
    }

    @Override
    public void unassignUserFromClass(long classId, List<Long> userIds) {
        log.info("Service: Unassigning User with id {} from Class with id {}", userIds, classId);
        Class clazz = findById(classId);
        User user;
        if (userIds.isEmpty()) {
            return;
        }
        for (Long userId : userIds) {
            user = userService.findById(userId);
            if(user.getMyClass().getId() != classId){
                log.warn("Service: Can`t unassign Users with id {} from Class with id {} because this user not in this class", userId, classId);
                continue;
            }
            if (user.getRole().equals("STUDENT")) {
                clazz.getUsers().remove(user);
                userService.unassignClassFromUser(user);
            }
        }
        classRepository.save(clazz);
        log.info("Service: Unassigned User with id {} from Class with id {}", userIds, classId);
    }

    @Override
    public Page<Class> findAllBySchool(long schoolId, String name, int page, int size) {
        log.info("Service: Finding all classes from school {} with name {}", schoolId, name);
        Specification<Class> specification = isValid(name) ?
                ClassSpecification.bySchool(schoolId).and(ClassSpecification.byName(name)) : ClassSpecification.bySchool(schoolId);
        return classRepository.findAll(specification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")));
    }

    @Override
    public List<Class> findAllBySchool(long schoolId) {
        log.info("Service: Finding all classes from school {}", schoolId);
        return classRepository.findAll(ClassSpecification.bySchool(schoolId));
    }

    @Override
    public Class findByUser(User user) {
        log.info("Service: Finding Classes with id {}", user.getId());
        List<Class> clazz = classRepository.findAll(ClassSpecification.hasUser(user));
        return clazz.isEmpty() ? null : clazz.get(0);
    }
}

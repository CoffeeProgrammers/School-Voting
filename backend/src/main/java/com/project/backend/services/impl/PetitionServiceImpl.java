package com.project.backend.services.impl;

import com.project.backend.models.enums.Status;
import com.project.backend.models.petitions.Petition;
import com.project.backend.models.voting.Voting;
import com.project.backend.repositories.petitions.PetitionRepository;
import com.project.backend.repositories.specification.PetitionSpecification;
import com.project.backend.repositories.specification.VotingSpecification;
import com.project.backend.services.inter.PetitionService;
import com.project.backend.services.inter.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.project.backend.utils.SpecificationUtil.addSpecification;
import static com.project.backend.utils.SpecificationUtil.isValid;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {
    private final PetitionRepository petitionRepository;
    private final UserService userService;

    @Override
    public Petition create(Petition petition) {
        log.info("Service: Creating a new petition {}", petition);
        return petitionRepository.save(petition);
    }

    @Override
    public void delete(long id) {
        log.info("Service: Deleting a petition {}", id);
        findById(id);
        petitionRepository.deleteById(id);
    }

    @Override
    public long support(long petitionId, long userId) {
        log.info("Service: Support for petition {} by user {}", petitionId, userId);
        Petition petition = findById(petitionId);
        petition.incrementCount();
        return petitionRepository.save(petition).getCount();
    }

    @Override
    public void approve(long petitionId) {
        log.info("Service: Approving a petition {}", petitionId);
        Petition petition = findById(petitionId);
        petition.setStatus(Status.APPROVED);
        petitionRepository.save(petition);
    }

    @Override
    public void reject(long petitionId) {
        log.info("Service: Rejecting a petition {}", petitionId);
        Petition petition = findById(petitionId);
        petition.setStatus(Status.REJECTED);
        petitionRepository.save(petition);
    }

    @Override
    public Petition findById(long id) {
        log.info("Service: Finding a petition {}", id);
        return petitionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Petition with id " + id + " not found"));
    }

    @Override
    public Page<Petition> findAllMy(String name, String status, int page, int size, Authentication auth) {
        log.info("Service: Finding all my petitions with name {} and status {}", name, status);
        return petitionRepository.findAll(
                createSpecification(name, status)
                        .and(PetitionSpecification.byCreator(
                                userService.findUserByAuth(auth)
                                        .getId()
                        )), PageRequest.of(
                                page, size, Sort.by(
                                        Sort.Direction.ASC, "endTime"
                                )
                        )
        );
    }

    @Override
    public Page<Petition> findAllByCreator(String name, String status, int page, int size, long creatorId) {
        log.info("Service: Finding all petitions by creator {}, name {} and status {}", creatorId, name, status);
        return petitionRepository.findAll(
                createSpecification(name, status)
                        .and(PetitionSpecification.byCreator(
                                creatorId
                        )), PageRequest.of(
                        page, size, Sort.by(
                                Sort.Direction.ASC, "endTime"
                        )
                )
        );
    }

    @Override
    public Page<Petition> findAllForDirector(String name, String status, int page, int size, Authentication auth) {
        log.info("Service: Finding all petitions for director, name {} and status {}", name, status);
        return petitionRepository.findAll(
                createSpecification(name, status), PageRequest.of(
                        page, size, Sort.by(
                                Sort.Direction.ASC, "endTime"
                        )
                )
        );
    }

    private Specification<Petition> createSpecification(String name, String status) {
        log.info("Service: Creating a petition {}", name + " " + status);
        Specification<Petition> specification = null;

        if (isValid(name)) {
            specification = addSpecification(specification, PetitionSpecification::byName, name);
        }
        if (isValid(status)) {
            specification = addSpecification(specification, PetitionSpecification::byStatus, status);
        }

        return specification;
    }
}

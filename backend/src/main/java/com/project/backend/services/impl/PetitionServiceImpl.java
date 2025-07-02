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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.project.backend.utils.SpecificationUtil.addSpecification;
import static com.project.backend.utils.SpecificationUtil.isValid;

@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {
    private final PetitionRepository petitionRepository;
    private final UserService userService;

    @Override
    public Petition create(Petition petition) {
        return petitionRepository.save(petition);
    }

    @Override
    public void delete(long id) {
        petitionRepository.deleteById(id);
    }

    @Override
    public long support(long petitionId, long userId) {
        Petition petition = findById(petitionId);
        petition.incrementCount();
        return petitionRepository.save(petition).getCount();
    }

    @Override
    public void approve(long petitionId) {
        Petition petition = findById(petitionId);
        petition.setStatus(Status.APPROVED);
        petitionRepository.save(petition);
    }

    @Override
    public void reject(long petitionId) {
        Petition petition = findById(petitionId);
        petition.setStatus(Status.REJECTED);
        petitionRepository.save(petition);
    }

    @Override
    public Petition findById(long id) {
        return petitionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Petition with id " + id + " not found"));
    }

    @Override
    public Page<Petition> findAllMy(String name, String status, int page, int size, Authentication auth) {
        Specification<Voting> specification = null;
        addSpecification(specification, VotingSpecification::ended);
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
        return petitionRepository.findAll(
                createSpecification(name, status), PageRequest.of(
                        page, size, Sort.by(
                                Sort.Direction.ASC, "endTime"
                        )
                )
        );
    }

    private Specification<Petition> createSpecification(String name, String status) {
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

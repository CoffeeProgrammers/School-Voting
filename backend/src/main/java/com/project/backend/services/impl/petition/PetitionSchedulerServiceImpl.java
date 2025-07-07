package com.project.backend.services.impl.petition;

import com.project.backend.models.enums.Status;
import com.project.backend.models.petition.Petition;
import com.project.backend.repositories.repos.petition.PetitionRepository;
import com.project.backend.repositories.specification.PetitionSpecification;
import com.project.backend.services.inter.petition.PetitionSchedulerService;
import com.project.backend.services.inter.petition.PetitionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PetitionSchedulerServiceImpl implements PetitionSchedulerService {

    private final PetitionRepository petitionRepository;
    private final PetitionService petitionService;

    @Scheduled(cron = "10 0 0 * * *")
    @Transactional
    public void checkAndUpdateExpiredPetitions() {
        log.info("Scheduler: Checking expired petitions");

        List<Petition> petitions = petitionRepository.findAll(
                PetitionSpecification.byStatus(Status.ACTIVE)
                        .and(PetitionSpecification.expired()));

        for (Petition petition : petitions) {
            if (!petition.now()) {
                petitionService.checkingStatus(petition);
                petitionRepository.save(petition);
                log.info("Scheduler: Petition {} status updated to {}", petition.getId(), petition.getStatus());
            }
        }
    }
}

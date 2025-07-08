package com.project.backend.services.impl.petition;

import com.project.backend.models.enums.Status;
import com.project.backend.models.petition.Petition;
import com.project.backend.repositories.repos.petition.PetitionRepository;
import com.project.backend.services.inter.petition.PetitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.mockito.Mockito.*;

class PetitionSchedulerServiceImplTest {

    @Mock
    private PetitionRepository petitionRepository;

    @Mock
    private PetitionService petitionService;

    @InjectMocks
    private PetitionSchedulerServiceImpl petitionSchedulerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkAndUpdateExpiredPetitions_shouldProcessExpiredPetitions() {
        Petition activeExpiredPetition = mock(Petition.class);
        when(activeExpiredPetition.now()).thenReturn(false);
        when(activeExpiredPetition.getId()).thenReturn(1L);
        when(activeExpiredPetition.getStatus()).thenReturn(Status.UNSUCCESSFUL);

        Petition activeNonExpiredPetition = mock(Petition.class);
        when(activeNonExpiredPetition.now()).thenReturn(true);

        when(petitionRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(activeExpiredPetition, activeNonExpiredPetition));

        petitionSchedulerService.checkAndUpdateExpiredPetitions();

        verify(petitionService).checkingStatus(activeExpiredPetition);
        verify(petitionRepository).save(activeExpiredPetition);

        verify(petitionService, never()).checkingStatus(activeNonExpiredPetition);
        verify(petitionRepository, never()).save(activeNonExpiredPetition);

        verify(petitionRepository).findAll(any(Specification.class));
    }
}

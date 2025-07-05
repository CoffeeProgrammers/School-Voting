package com.project.backend.mappers.petition;

import com.project.backend.dto.petition.PetitionFullResponse;
import com.project.backend.dto.petition.PetitionListResponse;
import com.project.backend.dto.petition.PetitionRequest;
import com.project.backend.models.petitions.Petition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetitionMapper {
    Petition fromRequestToPetition(PetitionRequest petitionRequest);
    PetitionFullResponse fromPetitionToFullResponse(Petition petition);
    PetitionListResponse fromPetitionToListResponse(Petition petition);
}

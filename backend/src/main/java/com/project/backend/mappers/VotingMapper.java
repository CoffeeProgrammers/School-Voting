package com.project.backend.mappers;


import com.project.backend.dto.voting.VotingCreateRequest;
import com.project.backend.dto.voting.VotingFullResponse;
import com.project.backend.dto.voting.VotingListResponse;
import com.project.backend.dto.voting.VotingUpdateRequest;
import com.project.backend.models.voting.Voting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VotingMapper {
    Voting fromRequestToVoting(VotingCreateRequest votingCreateRequest);
    Voting fromRequestToVoting(VotingUpdateRequest votingUpdateRequest);
    VotingFullResponse fromVotingToFullResponse(Voting voting);
    VotingListResponse fromVotingToListResponse(Voting voting);
}

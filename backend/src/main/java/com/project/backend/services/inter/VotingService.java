package com.project.backend.services.inter;

import com.project.backend.models.voting.Voting;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface VotingService {
    Voting create(Voting votingRequest);
    Voting update(Voting votingRequest);
    void delete(long id);
    Voting findById(long id);
    Page<Voting> findAllByUser(Authentication auth, String name, boolean now, boolean canVote, long page, long pageSize);
    Page<Voting> findAllByCreator(Authentication auth, String name, boolean now, long page, long pageSize);
    Page<Voting> findAllForDirector(Authentication auth, String name, long page, long pageSize);
    void vote(long votingId, Authentication auth);
}

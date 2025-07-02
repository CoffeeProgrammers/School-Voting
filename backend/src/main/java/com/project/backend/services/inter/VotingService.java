package com.project.backend.services.inter;

import com.project.backend.models.voting.Voting;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface VotingService {
    Voting create(Voting votingRequest, List<String> answer, List<Long> targetIds, long schoolId, Authentication authentication);

    Voting update(Voting votingRequest, List<String> answer, long id);
    void delete(long id);
    Voting findById(long id);
    Page<Voting> findAllByUser(Authentication auth, String name, Boolean now, Boolean canVote, int page, int size);
    Page<Voting> findAllByCreator(Authentication auth, String name, Boolean now, int page, int size);
    Page<Voting> findAllForDirector(Authentication auth, String name, int page, int size);
    void vote(long votingId, long answerId, Authentication auth);
}

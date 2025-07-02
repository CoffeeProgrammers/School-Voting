package com.project.backend.repositories.voting;

import com.project.backend.models.VotingUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingUserRepository extends JpaRepository<VotingUser, Long> {
    Long countAllByVoting_Id(Long id);
    Long countAllByVoting_IdAndAnswerNotNull(long votingId);
}

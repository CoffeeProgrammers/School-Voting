package com.project.backend.repositories.votings;

import com.project.backend.models.voting.VotingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotingUserRepository extends JpaRepository<VotingUser, VotingUser.VotingUserId> {
    Long countAllByVoting_Id(Long id);
    Long countAllByVoting_IdAndAnswerNotNull(long votingId);

    List<VotingUser> findAllByUser_Id(long userId);
}

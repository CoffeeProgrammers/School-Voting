package com.project.backend.repositories.repos.voting;

import com.project.backend.models.voting.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VotingRepository extends JpaRepository<Voting, Long>, JpaSpecificationExecutor<Voting> {
    void deleteAllByCreator_MyClass_Id(long userId);
    void deleteAllByCreator_School_Id(long schoolId);
}

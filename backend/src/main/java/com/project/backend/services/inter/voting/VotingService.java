package com.project.backend.services.inter.voting;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.voting.Voting;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VotingService {
    Voting create(Voting votingRequest, List<String> answer, List<Long> targetIds, long schoolId, long userId);
    Voting update(Voting votingRequest, List<String> answer, long id);
    void delete(long id);

    void deleteBy(LevelType levelType, long targetId);

    Voting findById(long id);

    List<Voting> findAllByUser(long userId);

    List<Voting> findAllByUserAndLevelClass(long userId);

    List<Voting> findAllByClass(long classId);

    Page<Voting> findAllByUser(long userId, String name, Boolean now, Boolean isNotVoted, int page, int size);
    Page<Voting> findAllByCreator(long userId, String name, Boolean now, Boolean notStarted, int page, int size);

    Page<Voting> findAllForDirector(long schoolId, long userId, String name, Boolean now, int page, int size);
    void vote(long votingId, long answerId, User user);

    void deletingUser(long userId);
}

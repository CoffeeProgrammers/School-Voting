package com.project.backend.models.voting;

import com.project.backend.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "voting_user")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class VotingUser {
    public VotingUser(Voting voting, User user) {
        VotingUserId votingUserId = new VotingUserId(voting.getId(), user.getId());
        this.setUser(user);
        this.setVoting(voting);
        this.setId(votingUserId);
    }

    public VotingUser(Voting voting, User user, Answer answer) {
        this(voting, user);
        this.setAnswer(answer);
    }

    @EmbeddedId
    private VotingUserId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("votingId")
    @ManyToOne
    @JoinColumn(name = "voting_id")
    private Voting voting;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @Embeddable
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class VotingUserId implements Serializable {
        private Long userId;
        private Long votingId;
    }
}

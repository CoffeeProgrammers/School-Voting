package com.project.backend.models;

import com.project.backend.models.voting.Answer;
import com.project.backend.models.voting.Voting;
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
}

@Embeddable
class VotingUserId implements Serializable {
    private Long userId;
    private Long votingId;
}

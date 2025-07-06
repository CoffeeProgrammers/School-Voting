package com.project.backend.models.google;

import com.project.backend.models.User;
import com.project.backend.models.voting.Voting;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_voting_events")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "voting"})
@EqualsAndHashCode(exclude = {"user", "voting"})
public class UserVotingEvent {

    @EmbeddedId
    private UserVotingEventId id;

    @ManyToOne
    @MapsId("votingId")
    @JoinColumn(name = "voting_id", nullable = false)
    private Voting voting;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String eventId;
    private String reminderEventId;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserVotingEventId implements Serializable {
        private Long userId;
        private Long votingId;
    }
}

package com.project.backend.models.google;

import com.project.backend.models.User;
import com.project.backend.models.petition.Petition;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_petition_events")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserPetitionEvent {

    @EmbeddedId
    private UserPetitionEventId id;

    @ManyToOne
    @MapsId("petitionId")
    @JoinColumn(name = "petition_id", nullable = false)
    private Petition petition;

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
    public static class UserPetitionEventId implements Serializable {
        private Long userId;
        private Long petitionId;
    }
}


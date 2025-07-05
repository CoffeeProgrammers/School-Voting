package com.project.backend.models.voting;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.google.UserVotingEvent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "votings")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"creator", "votingUsers"})
@EqualsAndHashCode(exclude = {"creator", "votingUsers"})
public class Voting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LevelType levelType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long countAll;
    @ManyToOne
    private User creator;
    @OneToMany(mappedBy = "voting")
    private Set<VotingUser> votingUsers = new HashSet<>();

    public boolean now(){
        return LocalDateTime.now().isAfter(this.startTime) && LocalDateTime.now().isBefore(this.endTime);
    }
    @OneToMany(mappedBy = "voting")
    private List<UserVotingEvent> userVotingEvents = new ArrayList<>();
}

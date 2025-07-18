package com.project.backend.models.voting;

import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.google.UserVotingEvent;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Entity
@Table(name = "votings")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"creator", "votingUsers", "userVotingEvents"})
@EqualsAndHashCode(exclude = {"creator", "votingUsers", "userVotingEvents"})
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
    private long targetId;
    @ManyToOne
    private School school;

    public boolean now(){
        log.info("Model: checking if voting is now going");
        return LocalDateTime.now().isAfter(this.startTime) && LocalDateTime.now().isBefore(this.endTime);
    }

    @OneToMany(mappedBy = "voting")
    private List<UserVotingEvent> userVotingEvents = new ArrayList<>();
}

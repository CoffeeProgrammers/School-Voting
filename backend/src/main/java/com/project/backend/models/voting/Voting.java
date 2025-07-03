package com.project.backend.models.voting;

import com.project.backend.models.User;
import com.project.backend.models.VotingUser;
import com.project.backend.models.enums.LevelType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    @ManyToOne
    private User creator;
    @OneToMany(mappedBy = "voting")
    private Set<VotingUser> votingUsers = new HashSet<>();
}

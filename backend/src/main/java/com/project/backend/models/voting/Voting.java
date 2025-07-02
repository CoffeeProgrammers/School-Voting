package com.project.backend.models.voting;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "votings")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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
}

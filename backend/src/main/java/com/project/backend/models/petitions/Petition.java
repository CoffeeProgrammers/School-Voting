package com.project.backend.models.petitions;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
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
public class Petition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    private User creator;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LevelType levelType;
    private LocalDateTime endTime;
    private Status status;
    private long count = 0;
}

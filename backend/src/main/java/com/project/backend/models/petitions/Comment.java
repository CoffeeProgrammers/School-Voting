package com.project.backend.models.petitions;

import com.project.backend.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User creator;
    @ManyToOne
    private Petition petition;
    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDateTime createdTime;
}

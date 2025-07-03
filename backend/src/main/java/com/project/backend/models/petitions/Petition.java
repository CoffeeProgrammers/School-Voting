package com.project.backend.models.petitions;

import com.project.backend.models.Class;
import com.project.backend.models.School;
import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Entity
@Table(name = "petitions")
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
    @ManyToMany(mappedBy = "petitions")
    private Set<User> users = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class myClass;

    public long incrementCount() {
        log.info("Model: Increment count {} of petition {}", count, this.id);
        return ++this.count;
    }
}

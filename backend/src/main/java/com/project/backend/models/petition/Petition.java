package com.project.backend.models.petition;

import com.project.backend.models.User;
import com.project.backend.models.enums.LevelType;
import com.project.backend.models.enums.Status;
import com.project.backend.models.google.UserPetitionEvent;
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
@Table(name = "petitions")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"users", "userPetitionEvents"})
@EqualsAndHashCode(exclude = {"creator", "userPetitionEvents"})
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
    private LocalDateTime creationTime;
    private LocalDateTime endTime;
    private Status status;
    private long count = 0;
    private long countNeeded = 0;
    @ManyToMany()
    @JoinTable(
            name = "petition_user",
            joinColumns = @JoinColumn(name = "petition_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();
    @OneToMany(mappedBy = "petition")
    private List<UserPetitionEvent> userPetitionEvents = new ArrayList<>();
    private long targetId;
    private String targetName;

    public void incrementCount() {
        log.info("Model: Increment count {} of petition {}", count, this.id);
        ++this.count;
    }
    public void decrementCount() {
        log.info("Model: Decrement count {} of petition {}", count, this.id);
        --this.count;
    }
    public boolean now(){
        return LocalDateTime.now().isBefore(this.endTime);
    }
}

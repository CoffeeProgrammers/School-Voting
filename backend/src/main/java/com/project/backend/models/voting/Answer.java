package com.project.backend.models.voting;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"voting"})
@EqualsAndHashCode(exclude = {"voting"})
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    private Voting voting;
    private long count;

    public Answer(String name, Voting voting) {
        this.name = name;
        this.voting = voting;
        this.count = 0;
    }

    public void incrementCount() {
        log.info("Model: Increment count {} for answer {}", count, this.id);
        ++this.count;
    }
}

package com.project.backend.models.voting;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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

    public long incrementCount() {
        return ++this.count;
    }
}

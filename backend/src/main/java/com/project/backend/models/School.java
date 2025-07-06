package com.project.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "schools")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"director", "classes"})
@EqualsAndHashCode
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToOne
    private User director;
    @OneToMany(mappedBy = "school")
    private Set<Class> classes;
}

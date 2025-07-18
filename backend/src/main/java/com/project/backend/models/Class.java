package com.project.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"users", "school"})
@EqualsAndHashCode(exclude = {"users", "school"})
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    private School school;
    @OneToMany(mappedBy = "myClass")
    private Set<User> users;
}

package com.project.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    private School school;
    @OneToMany
    private Set<User> users;
}

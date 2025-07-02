package com.project.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String keycloakUserId;
    @Column(unique = true, nullable = false)
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    public User(String keycloakUserId, String email, String firstName,
                String lastName, String role) {
        this.keycloakUserId = keycloakUserId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}

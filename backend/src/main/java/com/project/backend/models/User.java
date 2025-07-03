package com.project.backend.models;

import com.project.backend.models.petitions.Petition;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"myClass", "school", "votingUsers", "petitions"})
@EqualsAndHashCode(exclude = {"myClass", "school", "votingUsers", "petitions"})
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
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class myClass;
    @OneToMany(mappedBy = "user")
    private Set<VotingUser> votingUsers = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "petition_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "petition_id")
    )
    private Set<Petition> petitions = new HashSet<>();


    public User(String keycloakUserId, String email, String firstName,
                String lastName, String role) {
        this.keycloakUserId = keycloakUserId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}

package com.project.backend.models;

import com.project.backend.models.google.GoogleCalendarCredential;
import com.project.backend.models.google.UserPetitionEvent;
import com.project.backend.models.google.UserVotingEvent;
import com.project.backend.models.petition.Petition;
import com.project.backend.models.voting.VotingUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"myClass", "school", "votingUsers", "petitions", "googleCalendarCredential", "userVotingEvents", "userPetitionEvents"})
@EqualsAndHashCode(exclude = {"myClass", "school", "votingUsers", "petitions", "googleCalendarCredential", "userVotingEvents", "userPetitionEvents"})
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
    @ManyToMany(mappedBy = "users")
    private Set<Petition> petitions = new HashSet<>();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private GoogleCalendarCredential googleCalendarCredential;
    @OneToMany(mappedBy = "user")
    private List<UserVotingEvent> userVotingEvents = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<UserPetitionEvent> userPetitionEvents = new ArrayList<>();
}

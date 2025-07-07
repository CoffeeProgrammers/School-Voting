package com.project.backend.models.google;

import com.project.backend.models.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_calendars")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user"})
@EqualsAndHashCode(exclude = {"user"})
public class UserCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String calendarId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}

package com.greenloop.user_service.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "user_event_history")
public class UserEventHistory {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private UUID eventId;
    private String action; // "joined", "viewed", "liked"
    private Integer rating;
    private LocalDateTime timestamp = LocalDateTime.now();

    // Constructor
    public UserEventHistory(User user, UUID eventId, String action, Integer rating) {
        this.user = user;
        this.eventId = eventId;
        this.action = action;
        this.rating = rating;
    }
}
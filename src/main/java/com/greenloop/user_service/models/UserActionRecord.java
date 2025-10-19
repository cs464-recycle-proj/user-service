package com.greenloop.user_service.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenloop.user_service.enums.UserAction;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_action_records")
public class UserActionRecord {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnore
    private User user;

    private UUID eventId;
    private UserAction action; 
    private Integer rating;
    private LocalDateTime timestamp = LocalDateTime.now();

    // Constructor
    public UserActionRecord(User user, UUID eventId, UserAction action, Integer rating) {
        this.user = user;
        this.eventId = eventId;
        this.action = action;
        this.rating = rating;
    }
}
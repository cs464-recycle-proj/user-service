package com.greenloop.user_service.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "user_interests")
public class UserInterest {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private String interest;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructor
    public UserInterest(User user, String interest) {
        this.user = user;
        this.interest = interest;
    }
}

package com.greenloop.user_service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
@Builder
public class User {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDate joinedDate;

    private String avatarUrl;

    private String bio;

    private LocalDate birthDate;

    @Builder.Default
    private int eventsAttended = 0;

    @Builder.Default
    private int eventsRegistered = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<UserInterest> interests = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.joinedDate = LocalDate.now();
    }
}
package com.greenloop.user_service.models;

import java.time.LocalDate;
import com.greenloop.user_service.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    private String fullName;
    private String profileImg;
    private String bio;
    private LocalDate bday;
    private UserRole role;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    private List<UserInterest> interests;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    private List<UserEventHistory> eventHistory;

    // Constructor
    public User(String username, String email, UserRole role) {
        this.username = username;
        this.email = email; 
        this.role = role;
    }

    public User(String username, String email, String fullName, String profileImg, String bio, LocalDate bday, UserRole role) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.profileImg = profileImg;
        this.bio = bio;
        this.bday = bday;
        this.role = role;
    }
}
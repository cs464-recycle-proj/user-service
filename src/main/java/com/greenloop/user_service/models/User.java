package com.greenloop.user_service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {

    /* ----- Profile info ----- */
    @Id
    private UUID id;

    @Column(nullable=false, unique=true)
    private String name;

    @Column(nullable=false, unique=true)
    private String email;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate joinedDate;

    private String badge;

    // extra information
    private String avatarUrl;
    private String bio;
    private LocalDate bday;

    // user stats
    private int totalCoins = 0;
    private int eventsAttended = 0;
    private int eventsRegistered = 0;

    /* ----- Link to other tables ----- */
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<UserInterest> interests;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<UserActionRecord> actionHistory;

    /* ----- Constructors ----- */
    public User(String name, String email) {
        this.name = name;
        this.email = email; 
    }

    // public User(String name, String email, String profileImgUrl, String bio, LocalDate bday) {
    //     this.name = name;
    //     this.email = email;
    //     this.avatarUrl = profileImgUrl;
    //     this.bio = bio;
    //     this.bday = bday;
    // }
}
package com.greenloop.user_service.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenloop.user_service.enums.Interest;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user_interests")
@Builder
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Interest interest;
}

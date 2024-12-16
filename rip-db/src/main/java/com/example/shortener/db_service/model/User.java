package com.example.shortener.db_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String username;

    private String passwordHash;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Redirection> redirections;
}

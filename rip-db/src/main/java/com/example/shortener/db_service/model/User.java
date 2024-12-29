package com.example.shortener.db_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

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

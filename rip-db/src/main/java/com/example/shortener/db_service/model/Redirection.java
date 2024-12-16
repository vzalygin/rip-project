package com.example.shortener.db_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Entity
@Table(name = "redirections")
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(NON_NULL)
public class Redirection {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String shortKey;

    private String longUrl;

    private Date creationDate;

    private long usageCount;

    @ManyToOne
    private User user;
}

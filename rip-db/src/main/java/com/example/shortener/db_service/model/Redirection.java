package com.example.shortener.db_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "redirections")
@Data
@NoArgsConstructor
public class Redirection {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String shortKey;

    private String longUrl;

    private Date creationDate;
    private long usageCount;
}

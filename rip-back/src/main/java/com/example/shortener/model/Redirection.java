package com.example.shortener.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class Redirection {
    private Long id;
    private String shortKey;
    private String longUrl;
    private Date creationDate;
    private long usageCount;
    private long userId;

    public Redirection(String longUrl, String shortKey, long userId) {
        this.creationDate = new Date();
        this.longUrl = longUrl;
        this.shortKey = shortKey;
        this.userId = userId;
        this.usageCount = 0;
        this.id = null;
    }

    public void incrementUsageCount() {
        usageCount += 1;
    }
}

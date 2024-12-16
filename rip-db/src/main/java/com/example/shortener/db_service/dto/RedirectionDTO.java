package com.example.shortener.db_service.dto;

import com.example.shortener.db_service.model.Redirection;
import com.example.shortener.db_service.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.html.parser.Entity;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(NON_NULL)
public class RedirectionDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private Long id;
    private String shortKey;
    private String longUrl;
    @JsonSetter(nulls = Nulls.SKIP)
    private Date creationDate;
    private long usageCount;
    private long userId;

    public static RedirectionDTO fromEntity(Redirection r) {
        return new RedirectionDTO(
                r.getId(),
                r.getShortKey(),
                r.getLongUrl(),
                r.getCreationDate(),
                r.getUsageCount(),
                r.getUser().getId()
        );
    }

    public Redirection toEntity() {
        var entity = new Redirection();
        entity.setId(this.getId());
        entity.setShortKey(this.getShortKey());
        entity.setLongUrl(this.getLongUrl());
        entity.setUsageCount(this.getUsageCount());
        entity.setCreationDate(this.getCreationDate());
        var user = new User();
        user.setId(this.getUserId());
        entity.setUser(user);
        return entity;
    }
}

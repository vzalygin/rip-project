package com.example.shortener.db_service.dto;

import com.example.shortener.db_service.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(NON_NULL)
public class UserDTO {
    @JsonSetter(nulls = Nulls.SKIP)
    private Long id;
    private String username;
    private String passwordHash;

    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash()
        );
    }

    public User toEntity() {
        var user = new User();
        user.setId(this.getId());
        user.setUsername(this.getUsername());
        user.setPasswordHash(this.getPasswordHash());
        return user;
    }
}

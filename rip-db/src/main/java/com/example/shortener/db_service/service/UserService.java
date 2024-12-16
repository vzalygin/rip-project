package com.example.shortener.db_service.service;

import com.example.shortener.db_service.dto.UserDTO;
import com.example.shortener.db_service.model.User;
import com.example.shortener.db_service.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User save(UserDTO userDto) {
        var userEntity = new User();
        userEntity.setId(userDto.getId());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPasswordHash(userDto.getPasswordHash());
        return userRepo.save(userEntity);
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepo.findByUsername(username).map(UserDTO::fromEntity);
    }
}

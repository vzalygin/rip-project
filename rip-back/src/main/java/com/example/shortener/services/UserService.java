package com.example.shortener.services;

import com.example.shortener.model.ModelException;
import com.example.shortener.model.User;
import com.example.shortener.model.UserAlreadyExistsException;
import com.example.shortener.model.UserNotFoundException;
import com.example.shortener.repo.UserRepo;
import com.example.shortener.security.JwtTokenUtil;
import com.example.shortener.security.JwtUserDetailsService;
import com.example.shortener.security.UserDetails1;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordValidator passwordValidator;
    private final UserRepo userRepo;
    private final MessageDigest digest;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public void register(String username, String password) {
        passwordValidator.validateAndGetError(password);
        var hash = passwordEncoder.encode(makePasswordHash(password));;
        var user = new User(username, hash);
        userRepo.findByUsername(username).ifPresent(existentUser -> {
            throw new UserAlreadyExistsException(existentUser.getUsername());
        });
        userRepo.save(user);
    }

    public String authenticate(String username, String password) throws Exception {
        try {
            password = makePasswordHash(password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var userDetails = userDetailsService.loadUserByUsername(username);
            return jwtTokenUtil.generateToken(userDetails);
        } catch (DisabledException e) {
            throw new ModelException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new ModelException("INVALID_CREDENTIALS", e);
        }
    }

    private String makePasswordHash(String password) {
        var bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(bytes, StandardCharsets.UTF_8);
    }
}

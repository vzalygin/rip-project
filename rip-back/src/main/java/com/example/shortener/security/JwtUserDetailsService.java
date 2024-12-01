package com.example.shortener.security;

import com.example.shortener.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .map(x -> new UserDetails1(
                        x.getUsername(),
                        x.getPasswordHash(),
                        x.getId()
                )).orElseThrow(() ->
                        new UsernameNotFoundException("Could not found user with username = " + username)
                );
    }
}

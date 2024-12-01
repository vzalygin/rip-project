package com.example.shortener.controllers;

import com.example.shortener.messages.LoginResponse;
import com.example.shortener.messages.AuthenticateUserRequest;
import com.example.shortener.model.ModelException;
import com.example.shortener.security.JwtTokenUtil;
import com.example.shortener.security.JwtUserDetailsService;
import com.example.shortener.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/user/register")
    ResponseEntity<?> registerUser(@RequestBody AuthenticateUserRequest authenticateUserRequest) {
        var username = authenticateUserRequest.username();
        var password = authenticateUserRequest.password();

        if (isEmpty(username) || isEmpty(password)) {
            return ResponseEntity.badRequest().body(new ModelException("Invalid input"));
        }

        userService.register(username, password);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/login")
    LoginResponse login(@RequestBody AuthenticateUserRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.username(), authenticationRequest.password());
        var userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
        var token = jwtTokenUtil.generateToken(userDetails);
        return new LoginResponse(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new ModelException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new ModelException("INVALID_CREDENTIALS", e);
        }
    }
}

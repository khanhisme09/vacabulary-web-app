package com.vietdev.vocabularyapp.controller;

import com.vietdev.vocabularyapp.dto.RegistrationRequest;
import com.vietdev.vocabularyapp.model.User;
import com.vietdev.vocabularyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            User newUser = userService.registerNewUser(registrationRequest);
            return ResponseEntity.ok("User registered successfully! Username: " + newUser.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: Username or Email is already taken!");
        }
    }
}
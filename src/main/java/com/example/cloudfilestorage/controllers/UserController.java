package com.example.cloudfilestorage.controllers;

import com.example.cloudfilestorage.entity.UserEntity;
import com.example.cloudfilestorage.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute("user") UserEntity userEntity) {
        return ResponseEntity.ok(userService.register(userEntity));
    }
}

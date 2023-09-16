package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.entity.UserEntity;
import com.example.cloudfilestorage.exceptions.CustomBadRequestException;
import com.example.cloudfilestorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity register(UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new CustomBadRequestException("User exist!");
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }
}
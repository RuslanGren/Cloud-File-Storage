package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.exceptions.user.UserAlreadyExistsException;
import com.example.cloudfilestorage.domain.exceptions.user.UserNotFoundException;
import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.repository.UserRepository;
import com.example.cloudfilestorage.services.UserService;
import com.example.cloudfilestorage.web.mappers.UserMapper;
import com.example.cloudfilestorage.web.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class  UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public User createNewUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public String getUserFolder(UserDetails userDetails) {
        return String.format(
                "user-%d-files", userRepository.findByUsername(userDetails.getUsername()).orElseThrow().getId());
    }
}
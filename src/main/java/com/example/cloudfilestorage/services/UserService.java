package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.entity.UserEntity;
import com.example.cloudfilestorage.exceptions.UserNotFoundException;
import com.example.cloudfilestorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserEntity register(UserEntity user) {
        user.setPassword();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }

        return new User(userEntity.getUsername(), userEntity.getPassword(),
                Collections.emptyList()); // list of roles (simpled)
    }
}

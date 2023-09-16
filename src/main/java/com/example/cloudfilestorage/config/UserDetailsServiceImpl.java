package com.example.cloudfilestorage.config;

import com.example.cloudfilestorage.entity.UserEntity;
import com.example.cloudfilestorage.exceptions.UserNotFoundException;
import com.example.cloudfilestorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        return new User(userEntity.getUsername(), userEntity.getPassword(),
                Collections.emptyList()); // list of roles (simpled)
    }
}
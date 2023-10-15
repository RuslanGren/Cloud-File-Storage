package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.web.user.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User createNewUser(UserDto userDto);

    void delete(Long id);

    String getUserFolder(UserDetails userDetails);
}
package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.web.user.UserDto;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User create(UserDto userDto);

    void delete(Long id);
}
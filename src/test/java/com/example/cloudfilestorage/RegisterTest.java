package com.example.cloudfilestorage;

import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.repository.UserRepository;
import com.example.cloudfilestorage.services.UserService;
import com.example.cloudfilestorage.web.mappers.UserMapper;
import com.example.cloudfilestorage.web.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class RegisterTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testRegisterNewUser() {
        UserDto newUser = new UserDto();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        User user = userService.create(newUser);

        verify(userRepository).save(user);
    }

    @Test
    public void testRegisterExistingUser() {
        UserDto existingUser = new UserDto();
        existingUser.setUsername("testuser");
        existingUser.setPassword("password");

        User user = userMapper.toEntity(existingUser);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(CustomBadRequestException.class, () -> userService.create(existingUser));
    }
}
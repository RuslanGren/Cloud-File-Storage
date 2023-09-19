package com.example.cloudfilestorage;

import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.domain.exceptions.CustomBadRequestException;
import com.example.cloudfilestorage.repository.UserRepository;
import com.example.cloudfilestorage.services.UserService;
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

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testRegisterNewUser() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        userService.register(newUser);

        verify(userRepository).save(newUser);
    }

    @Test
    public void testRegisterExistingUser() {
        User existingUser = new User();
        existingUser.setUsername("testuser");
        existingUser.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));

        assertThrows(CustomBadRequestException.class, () -> userService.register(existingUser));
    }
}
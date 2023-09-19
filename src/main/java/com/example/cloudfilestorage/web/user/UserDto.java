package com.example.cloudfilestorage.web.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "Username should not be empty!")
    @Size(min = 3, max = 15, message = "Username should be between 3 and 15 characters")
    private String username;

    @NotBlank(message = "Password should not be empty!")
    @Size(min = 4, message = "Password should be minimum 4 characters")
    private String password;
}

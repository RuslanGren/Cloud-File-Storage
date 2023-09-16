package com.example.cloudfilestorage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username should not be empty!")
    @Size(min = 3, max = 15, message = "Username should be between 3 and 15 characters")
    private String username;
    @NotBlank(message = "Password should not be empty!")
    @Size(min = 4, message = "Password should be minimum 4 characters")
    private String password;
}

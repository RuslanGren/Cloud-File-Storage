package com.example.cloudfilestorage.web.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewNameFileDto {
    @NotBlank(message = "New name should not be empty!")
    @Size(min = 1, max = 32, message = "New name should be at most 32 characters")
    private String name;

    private String path;
}

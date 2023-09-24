package com.example.cloudfilestorage.web.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageNameDto {
    @NotBlank(message = "Package name should not be empty!")
    @Size(min = 4, max = 15, message = "Package name should be between 4 and 15 characters")
    private String name;
}

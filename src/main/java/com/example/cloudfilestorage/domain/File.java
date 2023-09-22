package com.example.cloudfilestorage.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

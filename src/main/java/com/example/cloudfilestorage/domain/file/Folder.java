package com.example.cloudfilestorage.domain.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "folders")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String path;

    @OneToMany(mappedBy = "folder")
    private List<File> files;

    @OneToMany(mappedBy = "parentFolder")
    private List<Folder> subFolders;

    @ManyToOne
    private Folder parentFolder;
}

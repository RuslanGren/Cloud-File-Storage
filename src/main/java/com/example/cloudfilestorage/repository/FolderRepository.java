package com.example.cloudfilestorage.repository;

import com.example.cloudfilestorage.domain.file.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByPath(String path);
    List<Folder> findByPathStartingWith(String path);
}

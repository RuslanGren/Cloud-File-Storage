package com.example.cloudfilestorage.repository;

import com.example.cloudfilestorage.domain.file.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByPathAndUserFolder(String path, String userFolder);
}

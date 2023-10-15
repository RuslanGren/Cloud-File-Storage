package com.example.cloudfilestorage.repository;

import com.example.cloudfilestorage.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByPathAndUserFolder(String path, String userFolder);

    boolean existsByName(String name);
}

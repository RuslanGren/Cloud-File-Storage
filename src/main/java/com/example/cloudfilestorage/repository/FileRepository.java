package com.example.cloudfilestorage.repository;

import com.example.cloudfilestorage.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findByPath(String path);
    List<File> findByPathStartingWith(String path);
}

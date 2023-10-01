package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.File;

import java.util.List;

public interface FileService {
    void createNewFile(String name, String path, String url);

    String deleteFileById(Long id);

    File getByPath(String path);

    List<File> findInFolder(String path);

    List<File> getAll();
}

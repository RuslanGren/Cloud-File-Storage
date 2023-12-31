package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.domain.file.Folder;
public interface FileService {
    void createNewFile(String name, Folder folder, String path);

    void deleteFileByPath(String path);

    void renameFileByPath(String path, String name, String updatedPath);

    boolean existByName(String name);

    File getByPath(String path);
}

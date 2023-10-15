package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.domain.file.Folder;
public interface FileService {
    void createNewFile(String name, Folder folder, String path, String url, String userFolder);

    void deleteFileByPath(String path, String userFolder);

    void renameFileByPath(String path, String name, String updatedPath, String userFolder);

    boolean existByName(String name);

    File getByPath(String path, String userFolder);
}

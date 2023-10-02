package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.domain.file.Folder;

public interface FileService {
    void createNewFile(String name, Folder folder, String path, String url);

    String deleteFileByIdAndReturnPath(Long id);

    File getByPath(String path);
}

package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.Folder;

public interface FolderService {

    void createRootFolder(Long userId);

    void createSubFolder(String name, String path);

    Folder getFolderByPath(String path);

    void removeFolder(String path);
}

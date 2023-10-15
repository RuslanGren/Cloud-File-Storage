package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.domain.file.Folder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {

    void upload(MultipartFile multipartFile, String path);

    void createSubFolder(String name, String path);

    void deleteFileByPath(String path);

    void renameFileByPath(String path, String name);

    Folder getFolderByPath(String path);

    File getFileByPath(String path);
}

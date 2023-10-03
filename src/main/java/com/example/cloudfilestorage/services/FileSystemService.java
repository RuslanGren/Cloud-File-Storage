package com.example.cloudfilestorage.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {

    void upload(MultipartFile multipartFile, String path);

    void createSubFolder(String name, String path);

    void deleteFileByPath(String path);

    void renameFileByPath(String path, String name);
}

package com.example.cloudfilestorage.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {

    void upload(MultipartFile multipartFile);

    void createNewFolder(String path);

    void deleteFileById(Long id);
}

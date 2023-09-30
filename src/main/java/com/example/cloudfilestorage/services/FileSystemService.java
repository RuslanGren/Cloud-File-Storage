package com.example.cloudfilestorage.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {

    void upload(MultipartFile multipartFile);

    void deleteFileById(Long id);
}

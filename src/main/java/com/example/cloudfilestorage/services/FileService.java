package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    void upload(MultipartFile multipartFile);

    File getById(Long id);

    List<File> getAll();

    void deleteFileById(Long id);
}

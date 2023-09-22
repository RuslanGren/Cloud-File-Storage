package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    void upload(MultipartFile multipartFile);

    List<File> getAll();
}

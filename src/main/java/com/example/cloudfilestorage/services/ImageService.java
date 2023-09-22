package com.example.cloudfilestorage.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String upload(MultipartFile multipartFile);

}

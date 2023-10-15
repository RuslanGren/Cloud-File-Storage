package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.domain.file.Folder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {

    void upload(MultipartFile multipartFile, String path, UserDetails userDetails);

    void createSubFolder(String name, String path, UserDetails userDetails);

    void deleteFileByPath(String path, UserDetails userDetails);

    void renameFileByPath(String path, String name, UserDetails userDetails);

    Folder getFolderByPath(String path, UserDetails userDetails);

    File getFileByPath(String path, UserDetails userDetails);
}

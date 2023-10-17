package com.example.cloudfilestorage.services;

import java.io.InputStream;

public interface MinioService {
    void createBucket();

    void saveFile(InputStream inputStream, String path);

    void deleteFile(String path);

    InputStream getFileContent(String path);

    void copyFile(String oldPath, String updatedPath);
}

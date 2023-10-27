package com.example.cloudfilestorage.services;

import io.minio.Result;
import io.minio.messages.Item;

import java.io.InputStream;

public interface MinioService {
    void createBucket();

    void saveFile(InputStream inputStream, String path);

    void deleteFile(String path);

    InputStream getFileContent(String path);

    void copyFile(String oldPath, String updatedPath);

    void removeFolder(Iterable<Result<Item>> listObjects);

    Iterable<Result<Item>> listObjects(String path);
}

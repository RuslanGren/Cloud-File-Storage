package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.services.MinioService;
import com.example.cloudfilestorage.services.properties.MinioProperties;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @SneakyThrows
    @Override
    public void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    @SneakyThrows
    @Override
    public void saveFile(InputStream inputStream, String path) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(path)
                .build());
    }

    @SneakyThrows
    @Override
    public void deleteFile(String path) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(path)
                .build());
    }

    @SneakyThrows
    @Override
    public InputStream getFileContent(String path) {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(path)
                .build());
    }

    @SneakyThrows
    @Override
    public void copyFile(String oldPath, String updatedPath) {
        minioClient.copyObject(CopyObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(updatedPath)
                .source(CopySource.builder()
                        .bucket(minioProperties.getBucket())
                        .object(oldPath)
                        .build())
                .build());
    }
}

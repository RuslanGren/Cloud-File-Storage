package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.services.MinioService;
import com.example.cloudfilestorage.services.properties.MinioProperties;
import io.minio.*;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

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

    @SneakyThrows
    @Override
    public Iterable<Result<Item>> listObjects(String path) {
        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(minioProperties.getBucket())
                .prefix(path)
                .build());
    }

    @SneakyThrows
    @Override
    public void removeFolder(Iterable<Result<Item>> listObjects) {
        List<DeleteObject> objects = new LinkedList<>();
        for (Result<Item> result : listObjects) {
            objects.add(new DeleteObject(result.get().objectName()));
        }
        minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(minioProperties.getBucket())
                .objects(objects)
                .build());

    }
}

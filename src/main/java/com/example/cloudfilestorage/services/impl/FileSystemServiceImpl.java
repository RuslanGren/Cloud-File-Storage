package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.exceptions.FileDeleteException;
import com.example.cloudfilestorage.domain.exceptions.FileUploadException;
import com.example.cloudfilestorage.services.FileService;
import com.example.cloudfilestorage.services.FileSystemService;
import com.example.cloudfilestorage.services.FolderService;
import com.example.cloudfilestorage.services.properties.MinioProperties;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileSystemServiceImpl implements FileSystemService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final FileService fileService;
    private final FolderService folderService;

    private final String DEFAULT_URL = minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/";

    @Override
    @Transactional
    public void deleteFileById(Long id) {
        try {
            String path = fileService.deleteFileById(id);
            deleteFile(path);
        } catch (Exception e) {
            throw new FileDeleteException("File delete failed " + e.getMessage());
        }
    }

    @Override
    public void upload(MultipartFile file) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new FileUploadException("Image upload failed " + e.getMessage());
        }
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileUploadException("Image must have name");
        }
        String fileName = file.getOriginalFilename();
        String path = fileName;
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new FileUploadException("Image upload failed " + e.getMessage());
        }
        saveFile(inputStream, path);
        fileService.createNewFile(fileName, path, DEFAULT_URL + path);
    }

    @SneakyThrows
    private void createBucket() {
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
    private void saveFile(InputStream inputStream, String path) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(path)
                .build());
    }

    @SneakyThrows
    public void deleteFile(String path) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(path)
                .build());
    }
}

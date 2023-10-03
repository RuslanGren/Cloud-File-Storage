package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.exceptions.FileDeleteException;
import com.example.cloudfilestorage.domain.exceptions.FileUploadException;
import com.example.cloudfilestorage.domain.file.Folder;
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

    @Transactional
    @Override
    public void renameFileByPath(String path, String name) {
        String fileType = path.substring(path.lastIndexOf("."));
        String updatedPath = path.substring(0, path.lastIndexOf("/")) + "/" + name + fileType;
        fileService.renameFileByPath(path, name, updatedPath); // update file in db
        InputStream inputStream = getFile(path); // get file from minio
        deleteFile(path); // delete file in minio
        saveFile(inputStream, updatedPath); // save the same file in minio but with updated path
    }

    @Transactional
    @Override
    public void deleteFileByPath(String path) {
        try {
            fileService.deleteFileByPath(path); // delete file in db
            deleteFile(path); // delete file in MinIO
        } catch (Exception e) {
            throw new FileDeleteException("File delete failed " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void upload(MultipartFile file, String path) {
        try {
            createBucket(); // create bucket in minio
        } catch (Exception e) {
            throw new FileUploadException("File upload failed " + e.getMessage());
        }
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileUploadException("File must have name");
        }
        String fileName = file.getOriginalFilename();
        Folder rootFolder = folderService.getFolderByPath(path); // get folder for file
        path = path + fileName; // path folder + fileName
        String url = minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/" + path;
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new FileUploadException("File upload failed " + e.getMessage());
        }
        saveFile(inputStream, path);
        fileService.createNewFile(fileName, rootFolder, path, url);
    }

    @Transactional
    @Override
    public void createSubFolder(String name, String path) {
        try {
            folderService.createSubFolder(name, path);
        } catch (Exception e) {
            throw new FileUploadException("Folder create failed" + e.getMessage());
        }
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

    @SneakyThrows
    public InputStream getFile(String path) {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(path)
                .build());
    }
}

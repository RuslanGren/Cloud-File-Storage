package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.exceptions.file.FileDeleteException;
import com.example.cloudfilestorage.domain.exceptions.file.FileNotFoundException;
import com.example.cloudfilestorage.domain.exceptions.file.FileRenameException;
import com.example.cloudfilestorage.domain.exceptions.file.FileUploadException;
import com.example.cloudfilestorage.domain.exceptions.folder.FolderCreateException;
import com.example.cloudfilestorage.domain.exceptions.folder.FolderNotFoundException;
import com.example.cloudfilestorage.domain.file.File;
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
        if (name.contains(" ")) {
            throw new FileRenameException("File should not contain spaces");
        }
        String oldName = path.substring(path.lastIndexOf("/") + 1);
        String fileType = path.substring(path.lastIndexOf("."));
        name = name + fileType; // new name with file type
        if (oldName.equals(name)) {
            throw new FileRenameException("Name must be different");
        }
        String updatedPath = path.substring(0, path.lastIndexOf("/")) + "/" + name;
        try {
            fileService.renameFileByPath(path, name, updatedPath); // update file in db
            copyFile(path, updatedPath); // copy file in minio and put with the new path
            deleteFile(path); // delete file with old path in minio
        } catch (Exception e) {
            throw new FileRenameException("File rename failed " + e.getMessage());
        }
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
        if (fileName.contains(" ")) {
            throw new FileUploadException("File should not contain spaces");
        }
        if (fileService.existByName(fileName)) {
            throw new FileUploadException("File with the same name already exists");
        }
        Folder rootFolder = folderService.getFolderByPath(path); // get folder for file
        path = path + fileName; // path folder + fileName
        String url = "http://localhost:9000" + "/" + minioProperties.getBucket() + "/" + path;
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
        if (name.contains(" ")) {
            throw new FileRenameException("Folder should not contain spaces");
        }
        try {
            folderService.createSubFolder(name, path);
        } catch (Exception e) {
            throw new FolderCreateException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public Folder getFolderByPath(String path) {
        try {
            return folderService.getFolderByPath(path);
        } catch (Exception e) {
            throw new FolderNotFoundException();
        }
    }

    @Transactional
    @Override
    public File getFileByPath(String path) {
        try {
            return fileService.getByPath(path);
        } catch (Exception e) {
            throw new FileNotFoundException();
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

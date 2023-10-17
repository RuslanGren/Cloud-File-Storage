package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.exceptions.file.*;
import com.example.cloudfilestorage.domain.exceptions.folder.FolderCreateException;
import com.example.cloudfilestorage.domain.exceptions.folder.FolderNotFoundException;
import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.domain.file.Folder;
import com.example.cloudfilestorage.services.FileService;
import com.example.cloudfilestorage.services.FileSystemService;
import com.example.cloudfilestorage.services.FolderService;
import com.example.cloudfilestorage.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileSystemServiceImpl implements FileSystemService {
    private final FileService fileService;
    private final FolderService folderService;
    private final MinioService minioService;

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
            minioService.copyFile(path, updatedPath); // copy file in minio and put with the new path
            minioService.deleteFile(path); // delete file with old path in minio
        } catch (Exception e) {
            throw new FileRenameException("File rename failed " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteFileByPath(String path) {
        try {
            fileService.deleteFileByPath(path); // delete file in db
            minioService.deleteFile(path); // delete file in MinIO
        } catch (Exception e) {
            throw new FileDeleteException("File delete failed " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void upload(MultipartFile file, String path) {
        try {
            minioService.createBucket(); // create bucket in minio
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
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new FileUploadException("File upload failed " + e.getMessage());
        }
        minioService.saveFile(inputStream, path);
        fileService.createNewFile(fileName, rootFolder, path);
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

    @Transactional
    @Override
    public InputStream getFileContent(String path) {
        try {
            return minioService.getFileContent(path);
        } catch (Exception e) {
            throw new FileDownloadException();
        }
    }

}
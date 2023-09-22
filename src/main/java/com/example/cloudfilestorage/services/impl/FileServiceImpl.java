package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.domain.exceptions.FileUploadException;
import com.example.cloudfilestorage.repository.FileRepository;
import com.example.cloudfilestorage.services.FileService;
import com.example.cloudfilestorage.services.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final FileRepository fileRepository;

    @Override
    public List<File> getAll() {
        return fileRepository.findAll();
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
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new FileUploadException("Image upload failed " + e.getMessage());
        }
        saveImage(inputStream, fileName);
        File thisFile = File.builder()
                .name(fileName)
                .url("http://localhost:9000/" + minioProperties.getBucket() + "/" + fileName)
                .build();
        fileRepository.save(thisFile);
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
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}

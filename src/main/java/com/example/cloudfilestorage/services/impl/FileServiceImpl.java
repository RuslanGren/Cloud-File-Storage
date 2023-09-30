package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.repository.FileRepository;
import com.example.cloudfilestorage.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    @Override
    public void createNewFile(String name, String path, String url) {
        File file = File.builder()
                .name(name)
                .path(path)
                .url(url)
                .build();
        fileRepository.save(file);
    }

    @Override
    public String deleteFileById(Long id) {
        File file = fileRepository.findById(id).orElseThrow();
        fileRepository.delete(file);
        return file.getPath();
    }

    @Override
    public File getById(Long id) {
        return fileRepository.findById(id).orElseThrow();
    }

    @Override
    public List<File> getAll() {
        return fileRepository.findAll();
    }
}

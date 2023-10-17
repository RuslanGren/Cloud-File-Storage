package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.exceptions.file.FileNotFoundException;
import com.example.cloudfilestorage.domain.file.File;
import com.example.cloudfilestorage.domain.file.Folder;
import com.example.cloudfilestorage.repository.FileRepository;
import com.example.cloudfilestorage.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    @Override
    public boolean existByName(String name) {
        return fileRepository.existsByName(name);
    }

    @Override
    public void createNewFile(String name, Folder folder, String path) {
        File file = File.builder()
                .name(name)
                .path(path)
                .localePath(folder.getLocalePath() + name)
                .folder(folder)
                .build();
        fileRepository.save(file);
    }

    @Override
    public void renameFileByPath(String path, String name, String updatedPath) {
        File file = fileRepository.findByPath(path).orElseThrow(FileNotFoundException::new);
        file.setName(name);
        file.setPath(updatedPath);
        file.setLocalePath(updatedPath.substring(updatedPath.indexOf("/") + 1));
        fileRepository.save(file);
    }

    @Override
    public void deleteFileByPath(String path) {
        File file = fileRepository.findByPath(path).orElseThrow(FileNotFoundException::new);
        fileRepository.delete(file);
    }

    @Override
    public File getByPath(String path) {
        File file = fileRepository.findByPath(path).orElseThrow(FileNotFoundException::new);
        if (file == null) {
            throw new FileNotFoundException();
        }
        return file;
    }
}

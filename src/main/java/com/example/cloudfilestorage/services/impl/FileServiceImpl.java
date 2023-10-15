package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.exceptions.FileNotFoundException;
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
    public void createNewFile(String name, Folder folder, String path, String url, String userFolder) {
        File file = File.builder()
                .name(name)
                .path(path)
                .url(url)
                .folder(folder)
                .userFolder(userFolder)
                .build();
        fileRepository.save(file);
    }

    @Override
    public void renameFileByPath(String path, String name, String updatedPath, String userFolder) {
        File file = fileRepository.findByPathAndUserFolder(path, userFolder).orElseThrow(FileNotFoundException::new);
        file.setName(name);
        file.setPath(updatedPath);
        file.setUrl(String.format("%s/%s", file.getUrl().substring(0, file.getUrl().lastIndexOf("/")), name));
        fileRepository.save(file);
    }

    @Override
    public void deleteFileByPath(String path, String userFolder) {
        File file = fileRepository.findByPathAndUserFolder(path, userFolder).orElseThrow(FileNotFoundException::new);
        fileRepository.delete(file);
    }

    @Override
    public File getByPath(String path, String userFolder) {
        return fileRepository.findByPathAndUserFolder(path, userFolder).orElseThrow(FileNotFoundException::new);
    }
}

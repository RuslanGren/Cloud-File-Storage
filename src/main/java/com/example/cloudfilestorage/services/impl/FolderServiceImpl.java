package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.file.Folder;
import com.example.cloudfilestorage.repository.FolderRepository;
import com.example.cloudfilestorage.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;

    @Override
    public void createNewFolder(String name, String path) {
        Folder folder = Folder.builder()
                .name(name)
                .path(path)
                .build();
        folderRepository.save(folder);
    }
}

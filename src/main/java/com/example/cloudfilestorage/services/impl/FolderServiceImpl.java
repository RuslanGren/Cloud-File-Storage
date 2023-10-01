package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.file.Folder;
import com.example.cloudfilestorage.repository.FolderRepository;
import com.example.cloudfilestorage.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;

    @Override
    public void createRootFolder(String name) {
        Folder folder = Folder.builder()
                .name(name)
                .path(name + "/")
                .build();
        folderRepository.save(folder);
    }

    @Override
    public void createSubFolder(String name, String path) {
        Folder rootFolder = folderRepository.findByPath(path);

        Folder subFolder = Folder.builder()
                .name(name)
                .path(path + name + "/")
                .build();

        // save subfolder in db
        folderRepository.save(subFolder);
        // add subfolder in rootfolder
        List<Folder> updatedSubFoldersList = rootFolder.getSubFolders();
        updatedSubFoldersList.add(subFolder);
        rootFolder.setSubFolders(updatedSubFoldersList);
        // save rootfolder after update
        folderRepository.save(rootFolder);
    }

    @Override
    public Folder getFolderByPath(String path) {
        return folderRepository.findByPath(path);
    }
}

package com.example.cloudfilestorage.services.impl;

import com.example.cloudfilestorage.domain.exceptions.FolderCreateException;
import com.example.cloudfilestorage.domain.file.Folder;
import com.example.cloudfilestorage.repository.FolderRepository;
import com.example.cloudfilestorage.services.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;

    @Override
    public void createRootFolder(Long userId) {
        String userFolder = "user-" + userId + "-files";
        Folder folder = Folder.builder()
                .name("root")
                .path(userFolder + "/root/")
                .localePath("root/")
                .build();
        folderRepository.save(folder);
    }

    @Transactional
    @Override
    public void createSubFolder(String name, String path) {
        Folder rootFolder = folderRepository.findByPath(path);
        List<Folder> subFolders = rootFolder.getSubFolders();

        if (subFolders.stream().anyMatch(subFolder -> subFolder.getName().equals(name))) {
            throw new FolderCreateException("There is already a folder with this name in this location");
        }

        Folder subFolder = Folder.builder()
                .name(name)
                .path(path + name + "/")
                .localePath(rootFolder.getLocalePath() + name + "/")
                .build();

        // save subfolder in db
        folderRepository.save(subFolder);
        // add subfolder in rootfolder
        subFolders.add(subFolder);
        rootFolder.setSubFolders(subFolders);
        // save rootfolder after update
        folderRepository.save(rootFolder);
    }

    @Override
    public Folder getFolderByPath(String path) {
        return folderRepository.findByPath(path);
    }
}

package com.example.cloudfilestorage.services.impl;

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
        Folder folder = Folder.builder()
                .name("root")
                .path("root/")
                .userFolder("user-" + userId + "-files")
                .build();
        folderRepository.save(folder);
    }

    @Transactional
    @Override
    public void createSubFolder(String name, String path, String userFolder) {
        Folder rootFolder = folderRepository.findByPathAndUserFolder(path, userFolder);

        Folder subFolder = Folder.builder()
                .name(name)
                .path(path + name + "/")
                .userFolder(userFolder)
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
    public Folder getFolderByPath(String path, String userFolder) {
        return folderRepository.findByPathAndUserFolder(path, userFolder);
    }
}

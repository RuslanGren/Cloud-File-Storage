package com.example.cloudfilestorage.services;

import com.example.cloudfilestorage.domain.file.Folder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface FolderService {
    void createNewFolder(String name, String path);
}

package com.example.cloudfilestorage.domain.exceptions.folder;

public class FolderDeleteException extends RuntimeException {
    public FolderDeleteException(String message) {
        super(message);
    }
}

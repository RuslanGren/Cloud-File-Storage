package com.example.cloudfilestorage.domain.exceptions.file;

public class FileRenameException extends RuntimeException {
    public FileRenameException(String message) {
        super(message);
    }
}

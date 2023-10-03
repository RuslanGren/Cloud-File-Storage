package com.example.cloudfilestorage.domain.exceptions;

public class FileRenameException extends RuntimeException {
    public FileRenameException(String message) {
        super(message);
    }
}

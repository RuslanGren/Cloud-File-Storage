package com.example.cloudfilestorage.domain.exceptions.file;

public class FileDeleteException extends RuntimeException {
    public FileDeleteException(String message) {
        super(message);
    }
}

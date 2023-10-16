package com.example.cloudfilestorage.domain.exceptions.file;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super(message);
    }
}

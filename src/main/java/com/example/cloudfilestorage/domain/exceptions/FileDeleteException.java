package com.example.cloudfilestorage.domain.exceptions;

public class FileDeleteException extends RuntimeException {
    public FileDeleteException(String message) {
        super(message);
    }
}

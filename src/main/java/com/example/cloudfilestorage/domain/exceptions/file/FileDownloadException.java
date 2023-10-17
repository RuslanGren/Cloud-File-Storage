package com.example.cloudfilestorage.domain.exceptions.file;

public class FileDownloadException extends RuntimeException {
    public FileDownloadException() {
        super("File download exception");
    }
}

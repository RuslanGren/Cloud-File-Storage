package com.example.cloudfilestorage.domain.exceptions.folder;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Folder not found!")
public class FolderNotFoundException extends RuntimeException {
}

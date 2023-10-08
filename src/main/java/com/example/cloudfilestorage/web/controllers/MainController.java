package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.services.FileService;
import com.example.cloudfilestorage.services.FileSystemService;
import com.example.cloudfilestorage.services.FolderService;
import com.example.cloudfilestorage.web.file.FolderDto;
import com.example.cloudfilestorage.web.file.NewNameFileDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final FileService fileService;
    private final FileSystemService fileSystemService;
    private final FolderService folderService;

    @GetMapping("/main")
    public String displayMainPage() {
        return "main";
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path,
            RedirectAttributes redirectAttributes) {
        try {
            fileSystemService.upload(file, path);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/search/" + path;
    }

    @PostMapping("/folder-create")
    public String createNewFolder(
            @ModelAttribute("package") @Valid FolderDto folderDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getDefaultMessage());
            }
        } else {
            fileSystemService.createSubFolder(folderDto.getName(), folderDto.getPath());
        }
        return "redirect:/search/" + folderDto.getPath();
    }

    @GetMapping("/search/**")
    public String getFolder(HttpServletRequest request, Model model) {
        String path = request.getRequestURL().toString().split("/search/")[1];
        model.addAttribute("folderDto", new FolderDto());
        model.addAttribute("folder", folderService.getFolderByPath(path));
        return "folder";
    }

    @GetMapping("/select/**")
    public String getFile(HttpServletRequest request, Model model) {
        String path = request.getRequestURL().toString().split("/select/")[1];
        model.addAttribute("newNameFileDto", new NewNameFileDto());
        model.addAttribute("file", fileService.getByPath(path));
        return "file";
    }

    @DeleteMapping("/delete/**")
    public String deleteFile(HttpServletRequest request) {
        String path = request.getRequestURL().toString().split("/delete/")[1];
        fileSystemService.deleteFileByPath(path);

        return String.format("redirect:/search/%s/", path.substring(0, path.lastIndexOf("/")));
        // get path of the folder and go to the folder where the file was located
    }

    @PatchMapping("/rename-file/**")
    public String renameFile(@ModelAttribute("newNameFileDto") @Valid NewNameFileDto newNameFileDto,
                             HttpServletRequest request,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        String path = request.getRequestURL().toString().split("/rename-file/")[1];

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getDefaultMessage());
            }
            return "redirect:/select/" + path;
        } else {
            fileSystemService.renameFileByPath(path, newNameFileDto.getName());
            return String.format("redirect:/search/%s/", path.substring(0, path.lastIndexOf("/")));
        }
    }
}

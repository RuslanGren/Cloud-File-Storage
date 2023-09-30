package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.services.FileService;
import com.example.cloudfilestorage.services.FileSystemService;
import com.example.cloudfilestorage.services.FolderService;
import com.example.cloudfilestorage.web.file.PackageNameDto;
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
    public String displayMainPage(Model model) {
        model.addAttribute("packages");
        model.addAttribute("folders", folderService.getAll());
        model.addAttribute("files", fileService.getAll());
        model.addAttribute("folder", new PackageNameDto());
        return "main";
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path,
            RedirectAttributes redirectAttributes) {
        try {
            fileSystemService.upload(file);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/main";
    }

    @PostMapping("/folder-create")
    public String createNewFolder(@ModelAttribute("package") @Valid PackageNameDto packageNameDto,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getDefaultMessage());
            }
        }
        fileSystemService.createNewFolder(packageNameDto.getName());
        return "redirect:/main";
    }

    @GetMapping("/search/{path}/")
    public String getFolder(@PathVariable("path") String path, Model model) {
        model.addAttribute("parent_folder", folderService.getFolderByPath(path));
        model.addAttribute("folders", folderService.getFolderByPathStartingWith(path));
        model.addAttribute("files", fileService.getFileByPathStartingWith(path));
        return "folder";
    }

    @GetMapping("/search/{path}")
    public String getFile(@PathVariable("path") String path, Model model) {
        model.addAttribute("file", fileService.getByPath(path));
        return "file";
    }

    @PostMapping("/{id}/delete")
    public String deleteFileById(@PathVariable("id") Long id) {
        fileService.deleteFileById(id);
        return "redirect:/main";
    }
}

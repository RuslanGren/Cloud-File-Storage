package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.services.FileService;
import com.example.cloudfilestorage.services.FileSystemService;
import com.example.cloudfilestorage.services.FolderService;
import com.example.cloudfilestorage.web.file.FolderDto;
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
    public String createNewFolder(@ModelAttribute("package") @Valid FolderDto folderDto,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {
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
        model.addAttribute("file", fileService.getByPath(path));
        return "file";
    }

    @PostMapping("/{id}/delete")
    public String deleteFileById(@PathVariable("id") Long id) {
        fileService.deleteFileById(id);
        return "redirect:/main";
    }
}

package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.services.FileService;
import com.example.cloudfilestorage.services.FileSystemService;
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

    @GetMapping("/main")
    public String displayMainPage(Model model) {
        model.addAttribute("packages");
        model.addAttribute("files", fileService.getAll());
        model.addAttribute("package", new PackageNameDto());
        return "main";
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            fileSystemService.upload(file);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/main";
    }

    @PostMapping("/package-create")
    public String createPackage(@ModelAttribute("package") @Valid PackageNameDto packageNameDto,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getDefaultMessage());
            }
        }
        return "redirect:/main";
    }

    @GetMapping("/{id}/show")
    public String getFileById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("file", fileService.getById(id));
        return "file";
    }

    @PostMapping("/{id}/delete")
    public String deleteFileById(@PathVariable("id") Long id) {
        fileService.deleteFileById(id);
        return "redirect:/main";
    }
}

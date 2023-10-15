package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.services.FileSystemService;
import com.example.cloudfilestorage.web.file.FolderDto;
import com.example.cloudfilestorage.web.file.NewNameFileDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final FileSystemService fileSystemService;

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            fileSystemService.upload(file, path, userDetails);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/search/" + path;
    }

    @PostMapping("/folder-create")
    public String createNewFolder(
            @ModelAttribute("package") @Valid FolderDto folderDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error_name", error.getDefaultMessage());
            }
        } else {
            try {
                fileSystemService.createSubFolder(folderDto.getName(), folderDto.getPath(), userDetails);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error_name", e.getMessage());
            }
        }
        return "redirect:/search/" + folderDto.getPath();
    }

    @GetMapping("/search/**")
    public String getFolder(HttpServletRequest request,
                            Model model,
                            @AuthenticationPrincipal UserDetails userDetails) {
        String path = request.getRequestURL().toString().split("/search/")[1];
        model.addAttribute("folder", fileSystemService.getFolderByPath(path, userDetails));
        model.addAttribute("folderDto", new FolderDto());
        return "folder";
    }

    @GetMapping("/select/**")
    public String getFile(HttpServletRequest request,
                          Model model,
                          @AuthenticationPrincipal UserDetails userDetails) {
        String path = request.getRequestURL().toString().split("/select/")[1];
        model.addAttribute("file", fileSystemService.getFileByPath(path, userDetails));
        model.addAttribute("newNameFileDto", new NewNameFileDto());
        return "file";
    }

    @DeleteMapping("/delete/**")
    public String deleteFile(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String path = request.getRequestURL().toString().split("/delete/")[1];
        fileSystemService.deleteFileByPath(path, userDetails);

        return String.format("redirect:/search/%s/", path.substring(0, path.lastIndexOf("/")));
        // get path of the folder and go to the folder where the file was located
    }

    @PatchMapping("/rename-file")
    public String renameFile(@ModelAttribute("newNameFileDto") @Valid NewNameFileDto newNameFileDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal UserDetails userDetails) {
        String path = newNameFileDto.getPath();
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error_name", error.getDefaultMessage());
            }
        } else {
            try {
                fileSystemService.renameFileByPath(path, newNameFileDto.getName(), userDetails);
                // if everything is ok, file renamed and go to the folder
                return String.format("redirect:/search/%s/", path.substring(0, path.lastIndexOf("/")));
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error_name", e.getMessage());
            }
        }
        return "redirect:/select/" + path;
    }
}

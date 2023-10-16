package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.domain.exceptions.file.FileNotFoundException;
import com.example.cloudfilestorage.domain.exceptions.folder.FolderNotFoundException;
import com.example.cloudfilestorage.services.FileSystemService;
import com.example.cloudfilestorage.services.UserService;
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
    private final UserService userService;

    @GetMapping("/search/")
    public String emptySearchRequestRedirect() {
        return "redirect:/search/root/";
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
        return "redirect:/search/" + path.substring(path.indexOf("/") + 1);
    }

    @PostMapping("/folder-create")
    public String createNewFolder(
            @ModelAttribute("package") @Valid FolderDto folderDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error_name", error.getDefaultMessage());
            }
        } else {
            try {
                fileSystemService.createSubFolder(folderDto.getName(), folderDto.getPath());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error_name", e.getMessage());
            }
        }
        return "redirect:/search/" + folderDto.getPath().substring(folderDto.getPath().indexOf("/") + 1);
    }

    @GetMapping("/search/**")
    public String getFolder(HttpServletRequest request,
                            Model model,
                            @AuthenticationPrincipal UserDetails userDetails) {
        String path = userService.getUserFolder(userDetails) +
                request.getRequestURL().toString().split("/search")[1];

        try {
            model.addAttribute("folder", fileSystemService.getFolderByPath(path));
            model.addAttribute("folderDto", new FolderDto());
            return "folder";
        } catch (FolderNotFoundException e) {
            model.addAttribute("error", "Error: Not found a folder at this path");
            return "error";
        }
    }

    @GetMapping("/select/**")
    public String getFile(HttpServletRequest request,
                          Model model,
                          @AuthenticationPrincipal UserDetails userDetails) {
        String path = userService.getUserFolder(userDetails) +
                request.getRequestURL().toString().split("/select")[1];
        try {
            model.addAttribute("file", fileSystemService.getFileByPath(path));
            model.addAttribute("newNameFileDto", new NewNameFileDto());
            return "file";
        } catch (FileNotFoundException e) {
            model.addAttribute("error", "Error: Not found a file at this path");
            return "error";
        }
    }

    @DeleteMapping("/delete/**")
    public String deleteFile(HttpServletRequest request,
                             @AuthenticationPrincipal UserDetails userDetails) {
        String filePath = request.getRequestURL().toString().split("/delete")[1];
        fileSystemService.deleteFileByPath(userService.getUserFolder(userDetails) + filePath);

        return String.format("redirect:/search/%s/", filePath.substring(1, filePath.lastIndexOf("/")));
        // get path of the folder and go to the folder where the file was located
    }

    @PatchMapping("/rename-file")
    public String renameFile(@ModelAttribute("newNameFileDto") @Valid NewNameFileDto newNameFileDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        String path = newNameFileDto.getPath();
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                redirectAttributes.addFlashAttribute("error_name", error.getDefaultMessage());
            }
        } else {
            try {
                fileSystemService.renameFileByPath(path, newNameFileDto.getName());
                // if everything is ok, file renamed and go to the folder
                return String.format("redirect:/search/%s/", path.substring(path.indexOf("/") + 1, path.lastIndexOf("/")));
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error_name", e.getMessage());
            }
        }
        return "redirect:/select/" + path.substring(path.indexOf("/") + 1);
    }
}

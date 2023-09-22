package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.domain.File;
import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.domain.exceptions.CustomBadRequestException;
import com.example.cloudfilestorage.repository.FileRepository;
import com.example.cloudfilestorage.services.ImageService;
import com.example.cloudfilestorage.services.UserService;
import com.example.cloudfilestorage.web.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ImageService imageService;
    private final FileRepository fileRepository;

    @GetMapping("/")
    public String displayIndexPage() {
        return "index";
    }

    @GetMapping("/main")
    public String displayMainPage(Model model) {
        List<File> files = fileRepository.findAll();
        model.addAttribute("files", files);
        return "main";
    }

    @GetMapping("/login")
    public String displayLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String displayRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") @Valid UserDto userDto,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.create(userDto);
            return "redirect:/main";
        } catch (CustomBadRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile multipartFile, Model model) {
        String name = imageService.upload(multipartFile);
        File file = new File();
        file.setName(name);
        fileRepository.save(file);
        return "redirect:/main";
    }
}

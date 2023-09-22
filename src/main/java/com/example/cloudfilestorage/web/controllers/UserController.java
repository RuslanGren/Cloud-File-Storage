package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.domain.exceptions.CustomBadRequestException;
import com.example.cloudfilestorage.services.FileService;
import com.example.cloudfilestorage.services.UserService;
import com.example.cloudfilestorage.web.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/")
    public String displayIndexPage() {
        return "index";
    }

    @GetMapping("/main")
    public String displayMainPage(Model model) {
        model.addAttribute("files", fileService.getAll());
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
    public String uploadImage(@RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        try {
            fileService.upload(file);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/main";
    }
}

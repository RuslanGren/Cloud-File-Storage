package com.example.cloudfilestorage.web.controllers;

import com.example.cloudfilestorage.domain.exceptions.CustomBadRequestException;
import com.example.cloudfilestorage.domain.user.User;
import com.example.cloudfilestorage.services.FolderService;
import com.example.cloudfilestorage.services.UserService;
import com.example.cloudfilestorage.web.user.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FolderService folderService;

    @GetMapping("/")
    public String displayIndexPage() {
        return "index";
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
            String rootFolderName =  String.format("user-%d-files", userService.getByUsername(userDto.getUsername()).getId());
            folderService.createNewFolder(rootFolderName, rootFolderName);
            return "redirect:/main";
        } catch (CustomBadRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
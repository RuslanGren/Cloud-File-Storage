package com.example.cloudfilestorage.controllers;

import com.example.cloudfilestorage.entity.UserEntity;
import com.example.cloudfilestorage.exceptions.CustomBadRequestException;
import com.example.cloudfilestorage.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public String displayIndexPage() {
        return "index";
    }

    @GetMapping("/main")
    public String displayMainPage() {
        return "main";
    }

    @GetMapping("/login")
    public String displayLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String displayRegisterPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") @Valid UserEntity userEntity,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // if there are errors in the form, redisplay the registration page
            return "register";
        }

        try {
            // attempt to register new user
            userService.register(userEntity);
            // if successful redirect to the index page
            return "redirect:/main";
        } catch (CustomBadRequestException e) {
            // if registration fails redisplay the registration page with error message
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}

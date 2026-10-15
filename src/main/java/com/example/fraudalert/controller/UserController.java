package com.example.fraudalert.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.fraudalert.model.Role;
import com.example.fraudalert.service.UserService;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("roles", Role.values());
        return "user-form";
    }

    @PostMapping
    public String create(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam Role role,
                         RedirectAttributes redirectAttributes) {
        if (userService.usernameExists(username)) {
            redirectAttributes.addFlashAttribute("error", "Username '" + username + "' already exists");
            return "redirect:/users/new";
        }
        userService.createUser(username, password, role);
        redirectAttributes.addFlashAttribute("message", "User '" + username + "' created successfully");
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "User deleted");
        return "redirect:/users";
    }
}

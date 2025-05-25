package com.example.travelappv2.controller;

import com.example.travelappv2.entity.Role;
import com.example.travelappv2.entity.User;
import com.example.travelappv2.repository.RoleRepository;
import com.example.travelappv2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
        }

        if (logout != null) {
            model.addAttribute("message", "Vous avez été déconnecté avec succès.");
        }

        // Vérifier si l'utilisateur est déjà connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/";
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                              BindingResult result,
                              Model model) {
        // Vérifier si le nom d'utilisateur ou l'email existe déjà
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Ce nom d'utilisateur est déjà utilisé");
        }

        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "Cet email est déjà utilisé");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            // Attribuer le rôle USER par défaut à l'utilisateur
            userService.registerUser(user, "USER");
            model.addAttribute("success", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue lors de l'inscription : " + e.getMessage());
            return "auth/register";
        }
    }
}

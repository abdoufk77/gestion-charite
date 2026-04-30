package com.emsi.gestioncharite.controller;

import com.emsi.gestioncharite.entity.Utilisateur;
import com.emsi.gestioncharite.repository.ActionChariteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ActionChariteRepository actionChariteRepository;

    @GetMapping({"/", "/home"})
    public String home(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("actions", actionChariteRepository.findAll());
        return "home";
    }

    @GetMapping("/donnateur/dashboard")
    public String donateurDashboard(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("actions", actionChariteRepository.findAll());
        return "donnateur/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        return "admin/dashboard";
    }

    @GetMapping("/superadmin/dashboard")
    public String superAdminDashboard(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        return "superadmin/dashboard";
    }
}

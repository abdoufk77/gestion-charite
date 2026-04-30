package com.emsi.gestioncharite.controller;

import com.emsi.gestioncharite.entity.Utilisateur;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        return "home";
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

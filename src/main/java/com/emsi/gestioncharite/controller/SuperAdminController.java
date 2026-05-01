package com.emsi.gestioncharite.controller;

import com.emsi.gestioncharite.entity.Utilisateur;
import com.emsi.gestioncharite.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("pendingOrganisations", superAdminService.getPendingOrganisations());
        return "superadmin/dashboard";
    }

    @PostMapping("/organisations/{id}/approuver")
    public String approuver(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            superAdminService.approuver(id);
            redirectAttributes.addFlashAttribute("success", "Organisation approuvée avec succès.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/superadmin/dashboard";
    }

    @PostMapping("/organisations/{id}/rejeter")
    public String rejeter(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            superAdminService.rejeter(id);
            redirectAttributes.addFlashAttribute("error", "Organisation rejetée.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/superadmin/dashboard";
    }
}

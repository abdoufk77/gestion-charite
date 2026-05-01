package com.emsi.gestioncharite.controller;

import com.emsi.gestioncharite.dto.request.OrganisationProfileRequest;
import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.service.OrganisationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/organisation")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;

    @GetMapping("/modifier")
    public String formulaire(@AuthenticationPrincipal AdminOrganisation admin, Model model) {
        OrganisationProfileRequest request = new OrganisationProfileRequest();
        request.setNom(admin.getOrganisation().getNom());
        request.setAdresseLegale(admin.getOrganisation().getAdresseLegale());
        request.setContactPrincipal(admin.getOrganisation().getContactPrincipal());
        request.setTelephone(admin.getOrganisation().getTelephone());
        request.setSiteWeb(admin.getOrganisation().getSiteWeb());
        request.setDescription(admin.getOrganisation().getDescription());
        request.setMission(admin.getOrganisation().getMission());
        model.addAttribute("org", request);
        model.addAttribute("utilisateur", admin);
        model.addAttribute("logoActuel", admin.getOrganisation().getLogoUrl());
        return "admin/organisation/modifier";
    }

    @PostMapping("/modifier")
    public String modifier(@AuthenticationPrincipal AdminOrganisation admin,
                           @Valid @ModelAttribute("org") OrganisationProfileRequest request,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("utilisateur", admin);
            model.addAttribute("logoActuel", admin.getOrganisation().getLogoUrl());
            return "admin/organisation/modifier";
        }
        organisationService.modifierProfil(admin.getOrganisation(), request);
        redirectAttributes.addFlashAttribute("succes", "Profil de l'organisation mis à jour avec succès !");
        return "redirect:/admin/profil";
    }
}

package com.emsi.gestioncharite.controller;

import com.emsi.gestioncharite.entity.Utilisateur;
import com.emsi.gestioncharite.enums.StatutOrg;
import com.emsi.gestioncharite.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @GetMapping({"/home", "/"})
    public String home(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("totalOrganisations", superAdminService.getAllOrganisations().size());
        model.addAttribute("activeOrganisations", superAdminService.countActiveOrganisations());
        model.addAttribute("pendingCount", superAdminService.getPendingOrganisations().size());
        model.addAttribute("totalDonateurs", superAdminService.getAllDonateurs().size());
        model.addAttribute("activeDonateurs", superAdminService.countActiveDonateurs());
        return "superadmin/home";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("pendingOrganisations", superAdminService.getPendingOrganisations());
        return "superadmin/dashboard";
    }

    private static final int PAGE_SIZE = 8;

    @GetMapping("/organisations")
    public String organisations(@AuthenticationPrincipal Utilisateur utilisateur,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "") String search,
                                @RequestParam(required = false) StatutOrg statut,
                                Model model) {
        var pageResult = superAdminService.searchOrganisations(search, statut, page, PAGE_SIZE);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("pageOrgs", pageResult);
        model.addAttribute("adminMap", superAdminService.getAdminOrgMap());
        model.addAttribute("activeCount", superAdminService.countActiveOrganisations());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("statut", statut);
        model.addAttribute("statuts", StatutOrg.values());
        return "superadmin/organisations";
    }

    @GetMapping("/donateurs")
    public String donateurs(@AuthenticationPrincipal Utilisateur utilisateur,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "") String search,
                            @RequestParam(required = false) Boolean enabled,
                            Model model) {
        var pageResult = superAdminService.searchDonateurs(search, enabled, page, PAGE_SIZE);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("pagedonateurs", pageResult);
        model.addAttribute("activeCount", superAdminService.countActiveDonateurs());
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("enabled", enabled);
        return "superadmin/donateurs";
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

    @PostMapping("/organisations/{id}/toggle-admin")
    public String toggleOrganisationAdmin(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            superAdminService.toggleOrganisationAdmin(id);
            redirectAttributes.addFlashAttribute("success", "Compte admin mis à jour.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/superadmin/organisations";
    }

    @PostMapping("/organisations/{id}/toggle-statut")
    public String toggleOrganisationStatut(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            superAdminService.toggleOrganisationStatut(id);
            redirectAttributes.addFlashAttribute("success", "Statut de l'organisation mis à jour.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/superadmin/organisations";
    }

    @PostMapping("/donateurs/{id}/toggle")
    public String toggleDonateur(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            superAdminService.toggleDonateur(id);
            redirectAttributes.addFlashAttribute("success", "Compte donateur mis à jour.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/superadmin/donateurs";
    }
}

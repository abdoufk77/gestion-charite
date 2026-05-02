package com.emsi.gestioncharite.controller;

import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Utilisateur;
import com.emsi.gestioncharite.enums.StatutParticipation;
import com.emsi.gestioncharite.repository.ActionChariteRepository;
import com.emsi.gestioncharite.repository.DonRepository;
import com.emsi.gestioncharite.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ActionChariteRepository actionChariteRepository;
    private final DonRepository donRepository;
    private final ParticipationRepository participationRepository;

    @GetMapping({"/", "/home"})
    public String home(@AuthenticationPrincipal Utilisateur utilisateur, Model model) {
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("actions", actionChariteRepository.findAll());
        return "home";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal AdminOrganisation admin,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {
        var pageable = PageRequest.of(page, 6, Sort.by("id").descending());
        var pageActions = actionChariteRepository.findByOrganisation(admin.getOrganisation(), pageable);
        model.addAttribute("utilisateur", admin);
        model.addAttribute("pageActions", pageActions);
        model.addAttribute("currentPage", page);
        model.addAttribute("nbEnAttente",
                participationRepository.countByOrganisationAndStatut(admin.getOrganisation(), StatutParticipation.EN_ATTENTE));
        model.addAttribute("totalCollecte",
                donRepository.sumMontantByOrganisation(admin.getOrganisation()));
        return "admin/dashboard";
    }

    @GetMapping("/admin/profil")
    public String adminProfil(@AuthenticationPrincipal AdminOrganisation admin, Model model) {
        model.addAttribute("utilisateur", admin);
        return "admin/profil";
    }

}

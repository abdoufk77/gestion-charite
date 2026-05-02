package com.emsi.gestioncharite.controller;

import com.emsi.gestioncharite.dto.request.ProfilDonateurRequest;
import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.enums.MethodePaiement;
import com.emsi.gestioncharite.enums.TypeAction;
import com.emsi.gestioncharite.repository.ActionChariteRepository;
import com.emsi.gestioncharite.service.DonateurService;
import com.emsi.gestioncharite.service.DonService;
import com.emsi.gestioncharite.service.ParticipationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/donnateur")
@RequiredArgsConstructor
public class DonateurController {

    private static final int HISTORIQUE_SIZE = 10;

    private final ActionChariteRepository actionChariteRepository;
    private final DonService donService;
    private final ParticipationService participationService;
    private final DonateurService donateurService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Donateur donateur, Model model) {
        List<ActionCharite> actions = actionChariteRepository.findAll();

        Map<Integer, Double> montantsCollectes = new HashMap<>();
        Map<Integer, Long> participationsCount = new HashMap<>();
        Set<Integer> actionsInscrites = new HashSet<>();

        for (ActionCharite action : actions) {
            if (action.getTypeAction() == TypeAction.FINANCIER) {
                montantsCollectes.put(action.getId(), donService.getMontantCollecte(action));
            } else if (action.getTypeAction() == TypeAction.PHYSIQUE) {
                participationsCount.put(action.getId(), participationService.getNombreParticipants(action.getId()));
                if (participationService.estInscrit(action.getId(), donateur)) {
                    actionsInscrites.add(action.getId());
                }
            }
        }

        model.addAttribute("utilisateur", donateur);
        model.addAttribute("actions", actions);
        model.addAttribute("montantsCollectes", montantsCollectes);
        model.addAttribute("participationsCount", participationsCount);
        model.addAttribute("actionsInscrites", actionsInscrites);
        return "donnateur/dashboard";
    }

    @GetMapping("/actions/{id}/don")
    public String formulaireDon(@PathVariable int id,
                                @AuthenticationPrincipal Donateur donateur,
                                Model model) {
        ActionCharite action = actionChariteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action introuvable"));

        if (action.getTypeAction() != TypeAction.FINANCIER) {
            return "redirect:/donnateur/dashboard";
        }

        double collecte = donService.getMontantCollecte(action);
        model.addAttribute("action", action);
        model.addAttribute("utilisateur", donateur);
        model.addAttribute("montantCollecte", collecte);
        model.addAttribute("methodes", MethodePaiement.values());
        return "donnateur/don-form";
    }

    @PostMapping("/actions/{id}/don")
    public String effectuerDon(@PathVariable int id,
                               @AuthenticationPrincipal Donateur donateur,
                               @RequestParam double montant,
                               @RequestParam MethodePaiement methode,
                               RedirectAttributes redirectAttributes) {
        if (montant <= 0) {
            redirectAttributes.addFlashAttribute("erreur", "Le montant doit être supérieur à 0");
            return "redirect:/donnateur/actions/" + id + "/don";
        }
        try {
            donService.effectuerDon(id, montant, methode, donateur);
            redirectAttributes.addFlashAttribute("succes", "Don de " + montant + " MAD effectué avec succès !");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/donnateur/dashboard";
    }

    @PostMapping("/actions/{id}/participer")
    public String participer(@PathVariable int id,
                             @AuthenticationPrincipal Donateur donateur,
                             RedirectAttributes redirectAttributes) {
        try {
            participationService.inscrire(id, donateur);
            redirectAttributes.addFlashAttribute("succes", "Inscription confirmée ! Votre participation est en attente de validation.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/donnateur/dashboard";
    }

    @GetMapping("/historique")
    public String historique(@AuthenticationPrincipal Donateur donateur,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
        var pageDons = donService.getHistorique(donateur, page, HISTORIQUE_SIZE);
        model.addAttribute("utilisateur", donateur);
        model.addAttribute("pageDons", pageDons);
        model.addAttribute("currentPage", page);
        return "donnateur/historique";
    }

    @GetMapping("/participations")
    public String mesParticipations(@AuthenticationPrincipal Donateur donateur,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model) {
        var pageParticipations = participationService.getMesParticipations(donateur, page, HISTORIQUE_SIZE);
        model.addAttribute("utilisateur", donateur);
        model.addAttribute("pageParticipations", pageParticipations);
        model.addAttribute("currentPage", page);
        return "donnateur/participations";
    }

    @PostMapping("/participations/{id}/annuler")
    public String annulerParticipation(@PathVariable int id,
                                       @AuthenticationPrincipal Donateur donateur,
                                       RedirectAttributes redirectAttributes) {
        try {
            participationService.annuler(id, donateur);
            redirectAttributes.addFlashAttribute("succes", "Votre participation a été annulée.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/donnateur/participations";
    }

    @GetMapping("/profil")
    public String profil(@AuthenticationPrincipal Donateur donateur, Model model) {
        ProfilDonateurRequest request = new ProfilDonateurRequest();
        request.setNom(donateur.getNom());
        request.setPrenom(donateur.getPrenom());
        request.setEmail(donateur.getEmail());

        model.addAttribute("utilisateur", donateur);
        model.addAttribute("profil", request);
        model.addAttribute("totalDons", donateurService.countDons(donateur));
        model.addAttribute("montantTotal", donateurService.sumMontantDons(donateur));
        model.addAttribute("totalParticipations", donateurService.countParticipations(donateur));
        return "donnateur/profil";
    }

    @PostMapping("/profil")
    public String updateProfil(@AuthenticationPrincipal Donateur donateur,
                               @Valid @ModelAttribute("profil") ProfilDonateurRequest request,
                               org.springframework.validation.BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("utilisateur", donateur);
            model.addAttribute("totalDons", donateurService.countDons(donateur));
            model.addAttribute("montantTotal", donateurService.sumMontantDons(donateur));
            model.addAttribute("totalParticipations", donateurService.countParticipations(donateur));
            return "donnateur/profil";
        }
        try {
            donateurService.updateProfil(request, donateur);
            redirectAttributes.addFlashAttribute("succes", "Profil mis à jour avec succès !");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/donnateur/profil";
    }
}

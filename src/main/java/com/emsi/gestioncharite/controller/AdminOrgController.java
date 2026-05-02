package com.emsi.gestioncharite.controller;

import com.emsi.gestioncharite.dto.request.ActionChariteRequest;
import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Participation;
import com.emsi.gestioncharite.enums.Categorie;
import com.emsi.gestioncharite.enums.StatutParticipation;
import com.emsi.gestioncharite.enums.TypeAction;
import com.emsi.gestioncharite.repository.DonRepository;
import com.emsi.gestioncharite.repository.ParticipationRepository;
import com.emsi.gestioncharite.service.ActionChariteService;
import com.emsi.gestioncharite.service.DonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/admin/actions")
@RequiredArgsConstructor
public class AdminOrgController {

    private static final int PAGE_SIZE = 6;

    private final ActionChariteService actionChariteService;
    private final DonService donService;
    private final DonRepository donRepository;
    private final ParticipationRepository participationRepository;

    @GetMapping
    public String liste(@AuthenticationPrincipal AdminOrganisation admin,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "") String search,
                        @RequestParam(required = false) Categorie categorie,
                        Model model) {
        Page<ActionCharite> pageResult = actionChariteService.search(admin, search, categorie, page, PAGE_SIZE);
        model.addAttribute("pageActions", pageResult);
        model.addAttribute("utilisateur", admin);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("categorie", categorie);
        model.addAttribute("categories", Arrays.asList(Categorie.values()));
        return "admin/actions/list";
    }

    @GetMapping("/nouvelle")
    public String formulaireCreation(Model model) {
        model.addAttribute("action", new ActionChariteRequest());
        model.addAttribute("categories", Categorie.values());
        model.addAttribute("typesAction", TypeAction.values());
        model.addAttribute("modeEdition", false);
        return "admin/actions/form";
    }

    @PostMapping("/nouvelle")
    public String creer(@AuthenticationPrincipal AdminOrganisation admin,
                        @Valid @ModelAttribute("action") ActionChariteRequest request,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        validerChampsConditionnels(request, result);
        if (result.hasErrors()) {
            model.addAttribute("categories", Categorie.values());
            model.addAttribute("typesAction", TypeAction.values());
            model.addAttribute("modeEdition", false);
            return "admin/actions/form";
        }
        if (request.getDateFin() != null && request.getDateDebut() != null
                && request.getDateFin().before(request.getDateDebut())) {
            result.rejectValue("dateFin", "err.date", "La date de fin doit être après la date de début");
            model.addAttribute("categories", Categorie.values());
            model.addAttribute("typesAction", TypeAction.values());
            model.addAttribute("modeEdition", false);
            return "admin/actions/form";
        }
        actionChariteService.creer(request, admin);
        redirectAttributes.addFlashAttribute("succes", "Action créée avec succès !");
        return "redirect:/admin/actions";
    }

    @GetMapping("/{id}/modifier")
    public String formulaireModification(@PathVariable int id,
                                         @AuthenticationPrincipal AdminOrganisation admin,
                                         Model model) {
        ActionCharite action = actionChariteService.getActionByIdAndAdmin(id, admin);
        ActionChariteRequest request = new ActionChariteRequest();
        request.setTitre(action.getTitre());
        request.setDescription(action.getDescription());
        request.setDateDebut(action.getDateDebut());
        request.setDateFin(action.getDateFin());
        request.setCategorie(action.getCategorie());
        request.setTypeAction(action.getTypeAction());
        request.setObjectifMontant(action.getObjectifMontant());
        request.setNombrePlacesMax(action.getNombrePlacesMax());
        model.addAttribute("action", request);
        model.addAttribute("actionId", id);
        model.addAttribute("imageActuelle", action.getImageUrl());
        model.addAttribute("categories", Categorie.values());
        model.addAttribute("typesAction", TypeAction.values());
        model.addAttribute("modeEdition", true);
        return "admin/actions/form";
    }

    @PostMapping("/{id}/modifier")
    public String modifier(@PathVariable int id,
                           @AuthenticationPrincipal AdminOrganisation admin,
                           @Valid @ModelAttribute("action") ActionChariteRequest request,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        validerChampsConditionnels(request, result);
        if (result.hasErrors()) {
            model.addAttribute("actionId", id);
            model.addAttribute("categories", Categorie.values());
            model.addAttribute("typesAction", TypeAction.values());
            model.addAttribute("modeEdition", true);
            return "admin/actions/form";
        }
        if (request.getDateFin() != null && request.getDateDebut() != null
                && request.getDateFin().before(request.getDateDebut())) {
            result.rejectValue("dateFin", "err.date", "La date de fin doit être après la date de début");
            model.addAttribute("actionId", id);
            model.addAttribute("categories", Categorie.values());
            model.addAttribute("typesAction", TypeAction.values());
            model.addAttribute("modeEdition", true);
            return "admin/actions/form";
        }
        actionChariteService.modifier(id, request, admin);
        redirectAttributes.addFlashAttribute("succes", "Action modifiée avec succès !");
        return "redirect:/admin/actions";
    }

    private void validerChampsConditionnels(ActionChariteRequest request, BindingResult result) {
        if (request.getTypeAction() == TypeAction.FINANCIER && request.getObjectifMontant() == null) {
            result.rejectValue("objectifMontant", "err.objectif", "L'objectif financier est obligatoire");
        }
        if (request.getTypeAction() == TypeAction.PHYSIQUE && request.getNombrePlacesMax() == null) {
            result.rejectValue("nombrePlacesMax", "err.places", "Le nombre de places est obligatoire");
        }
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable int id,
                            @AuthenticationPrincipal AdminOrganisation admin,
                            @RequestParam(defaultValue = "0") int page,
                            RedirectAttributes redirectAttributes) {
        actionChariteService.supprimer(id, admin);
        redirectAttributes.addFlashAttribute("succes", "Action supprimée avec succès !");
        return "redirect:/admin/actions?page=" + page;
    }

    // ── Détail action (dons ou participations) ────────────────────────────────

    @GetMapping("/{id}/detail")
    public String detail(@PathVariable int id,
                         @AuthenticationPrincipal AdminOrganisation admin,
                         @RequestParam(required = false) StatutParticipation statut,
                         Model model) {
        ActionCharite action = actionChariteService.getActionByIdAndAdmin(id, admin);
        model.addAttribute("action", action);
        model.addAttribute("utilisateur", admin);

        if (action.getTypeAction() == TypeAction.FINANCIER) {
            model.addAttribute("dons", donRepository.findByActionChariteOrderByDateDonDesc(action));
            model.addAttribute("montantCollecte", donService.getMontantCollecte(action));

        } else if (action.getTypeAction() == TypeAction.PHYSIQUE) {
            StatutParticipation filtreActif = statut != null ? statut : StatutParticipation.EN_ATTENTE;
            model.addAttribute("participations",
                    participationRepository.findByActionChariteAndStatutOrderByDateInscriptionDesc(action, filtreActif));
            model.addAttribute("nbEnAttente",
                    participationRepository.countByActionChariteAndStatut(action, StatutParticipation.EN_ATTENTE));
            model.addAttribute("nbConfirmees",
                    participationRepository.countByActionChariteAndStatut(action, StatutParticipation.CONFIRMEE));
            model.addAttribute("nbRefusees",
                    participationRepository.countByActionChariteAndStatut(action, StatutParticipation.REFUSEE));
            model.addAttribute("statutFiltre", filtreActif);
        }
        return "admin/actions/detail";
    }

    // ── Confirmer / Refuser une participation ─────────────────────────────────

    @PostMapping("/participations/{participationId}/confirmer")
    public String confirmerParticipation(@PathVariable int participationId,
                                         @AuthenticationPrincipal AdminOrganisation admin,
                                         RedirectAttributes redirectAttributes) {
        Participation p = participationRepository.findById(participationId)
                .orElseThrow(() -> new RuntimeException("Participation introuvable"));
        verifierAppartenance(p, admin);
        p.setStatut(StatutParticipation.CONFIRMEE);
        participationRepository.save(p);
        redirectAttributes.addFlashAttribute("succes", "Participation confirmée !");
        return "redirect:/admin/actions/" + p.getActionCharite().getId() + "/detail?statut=EN_ATTENTE";
    }

    @PostMapping("/participations/{participationId}/refuser")
    public String refuserParticipation(@PathVariable int participationId,
                                       @AuthenticationPrincipal AdminOrganisation admin,
                                       RedirectAttributes redirectAttributes) {
        Participation p = participationRepository.findById(participationId)
                .orElseThrow(() -> new RuntimeException("Participation introuvable"));
        verifierAppartenance(p, admin);
        p.setStatut(StatutParticipation.REFUSEE);
        participationRepository.save(p);
        redirectAttributes.addFlashAttribute("succes", "Participation refusée.");
        return "redirect:/admin/actions/" + p.getActionCharite().getId() + "/detail?statut=EN_ATTENTE";
    }

    private void verifierAppartenance(Participation p, AdminOrganisation admin) {
        if (p.getActionCharite().getOrganisation().getId() != admin.getOrganisation().getId()) {
            throw new RuntimeException("Accès refusé");
        }
    }
}

package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.dto.request.ActionChariteRequest;
import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.Categorie;
import com.emsi.gestioncharite.repository.ActionChariteRepository;
import com.emsi.gestioncharite.service.ActionChariteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActionChariteServiceImpl implements ActionChariteService {

    private final ActionChariteRepository actionChariteRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public Page<ActionCharite> getActionsByAdmin(AdminOrganisation admin, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return actionChariteRepository.findByOrganisation(admin.getOrganisation(), pageable);
    }

    @Override
    public Page<ActionCharite> search(AdminOrganisation admin, String search, Categorie categorie, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Organisation org = admin.getOrganisation();
        boolean hasSearch = StringUtils.hasText(search);
        boolean hasCat = categorie != null;

        if (hasSearch && hasCat) {
            return actionChariteRepository.findByOrganisationAndCategorieAndKeyword(org, categorie, search.trim(), pageable);
        } else if (hasSearch) {
            return actionChariteRepository.findByOrganisationAndKeyword(org, search.trim(), pageable);
        } else if (hasCat) {
            return actionChariteRepository.findByOrganisationAndCategorie(org, categorie, pageable);
        } else {
            return actionChariteRepository.findByOrganisation(org, pageable);
        }
    }

    @Override
    public ActionCharite getActionByIdAndAdmin(int id, AdminOrganisation admin) {
        ActionCharite action = actionChariteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action introuvable"));
        if (action.getOrganisation() == null || action.getOrganisation().getId() != admin.getOrganisation().getId()) {
            throw new RuntimeException("Accès refusé");
        }
        return action;
    }

    @Override
    @Transactional
    public void creer(ActionChariteRequest request, AdminOrganisation admin) {
        ActionCharite action = new ActionCharite();
        action.setTitre(request.getTitre());
        action.setDescription(request.getDescription());
        action.setDateDebut(request.getDateDebut());
        action.setDateFin(request.getDateFin());
        action.setCategorie(request.getCategorie());
        action.setTypeAction(request.getTypeAction());
        action.setOrganisation(admin.getOrganisation());
        appliquerChampsConditionnels(action, request);
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            action.setImageUrl(sauvegarderImage(request.getImage()));
        }
        actionChariteRepository.save(action);
    }

    @Override
    @Transactional
    public void modifier(int id, ActionChariteRequest request, AdminOrganisation admin) {
        ActionCharite action = getActionByIdAndAdmin(id, admin);
        action.setTitre(request.getTitre());
        action.setDescription(request.getDescription());
        action.setDateDebut(request.getDateDebut());
        action.setDateFin(request.getDateFin());
        action.setCategorie(request.getCategorie());
        action.setTypeAction(request.getTypeAction());
        appliquerChampsConditionnels(action, request);
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            action.setImageUrl(sauvegarderImage(request.getImage()));
        }
        actionChariteRepository.save(action);
    }

    private String sauvegarderImage(MultipartFile file) {
        try {
            Path dossier = Paths.get(uploadDir, "actions");
            Files.createDirectories(dossier);
            String ext = file.getOriginalFilename() != null && file.getOriginalFilename().contains(".")
                    ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'))
                    : ".jpg";
            String nomFichier = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), dossier.resolve(nomFichier));
            return "/uploads/actions/" + nomFichier;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
        }
    }

    private void appliquerChampsConditionnels(ActionCharite action, ActionChariteRequest request) {
        switch (request.getTypeAction()) {
            case FINANCIER -> {
                action.setObjectifMontant(request.getObjectifMontant());
                action.setNombrePlacesMax(null);
            }
            case PHYSIQUE -> {
                action.setNombrePlacesMax(request.getNombrePlacesMax());
                action.setObjectifMontant(null);
            }
        }
    }

    @Override
    @Transactional
    public void supprimer(int id, AdminOrganisation admin) {
        ActionCharite action = getActionByIdAndAdmin(id, admin);
        actionChariteRepository.delete(action);
    }
}

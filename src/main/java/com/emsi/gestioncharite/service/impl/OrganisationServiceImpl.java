package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.dto.request.OrganisationProfileRequest;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.repository.OrganisationRepository;
import com.emsi.gestioncharite.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {

    private final OrganisationRepository organisationRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    @Transactional
    public void modifierProfil(Organisation organisation, OrganisationProfileRequest request) {
        organisation.setNom(request.getNom());
        organisation.setAdresseLegale(request.getAdresseLegale());
        organisation.setContactPrincipal(request.getContactPrincipal());
        organisation.setTelephone(request.getTelephone());
        organisation.setSiteWeb(request.getSiteWeb());
        organisation.setDescription(request.getDescription());
        organisation.setMission(request.getMission());

        MultipartFile logo = request.getLogo();
        if (logo != null && !logo.isEmpty()) {
            organisation.setLogoUrl(sauvegarderLogo(logo));
        }

        organisationRepository.save(organisation);
    }

    private String sauvegarderLogo(MultipartFile file) {
        try {
            Path dossier = Paths.get(uploadDir, "logos");
            Files.createDirectories(dossier);

            String extension = obtenirExtension(file.getOriginalFilename());
            String nomFichier = UUID.randomUUID() + extension;
            Files.copy(file.getInputStream(), dossier.resolve(nomFichier));

            return "/uploads/logos/" + nomFichier;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du logo", e);
        }
    }

    private String obtenirExtension(String nomFichier) {
        if (nomFichier == null || !nomFichier.contains(".")) return ".png";
        return nomFichier.substring(nomFichier.lastIndexOf('.'));
    }
}

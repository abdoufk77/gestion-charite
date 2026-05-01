package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.dto.request.RegisterRequest;
import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.Role;
import com.emsi.gestioncharite.enums.StatutOrg;
import com.emsi.gestioncharite.repository.OrganisationRepository;
import com.emsi.gestioncharite.repository.UtilisateurRepository;
import com.emsi.gestioncharite.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final OrganisationRepository organisationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Les mots de passe ne correspondent pas");
        }
        if (request.getTypeCompte() == null || request.getTypeCompte().isBlank()) {
            throw new RuntimeException("Veuillez choisir un type de compte");
        }

        if ("ORGANISATION".equals(request.getTypeCompte())) {
            registerOrganisation(request);
        } else {
            registerDonateur(request);
        }
    }

    private void registerDonateur(RegisterRequest request) {
        Donateur donateur = new Donateur();
        donateur.setNom(request.getNom());
        donateur.setPrenom(request.getPrenom());
        donateur.setEmail(request.getEmail());
        donateur.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        donateur.setRole(Role.DONATEUR);
        utilisateurRepository.save(donateur);
    }

    private void registerOrganisation(RegisterRequest request) {
        Organisation org = new Organisation();
        org.setNom(request.getNomOrganisation());
        org.setAdresseLegale(request.getAdresseLegale());
        org.setNumIdentFiscale(request.getNumIdentFiscale());
        org.setDescription(request.getDescriptionOrg());
        org.setContactPrincipal(request.getEmail());
        org.setStatut(StatutOrg.EN_ATTENTE);
        organisationRepository.save(org);

        AdminOrganisation admin = new AdminOrganisation();
        admin.setNom(request.getNom());
        admin.setPrenom(request.getPrenom());
        admin.setEmail(request.getEmail());
        admin.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN_ORGANISATION);
        admin.setOrganisation(org);
        admin.setEnabled(false); // bloqué jusqu'à l'approbation du super admin
        utilisateurRepository.save(admin);
    }
}

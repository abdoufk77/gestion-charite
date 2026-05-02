package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.dto.request.ProfilDonateurRequest;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.enums.StatutParticipation;
import com.emsi.gestioncharite.repository.DonRepository;
import com.emsi.gestioncharite.repository.ParticipationRepository;
import com.emsi.gestioncharite.repository.UtilisateurRepository;
import com.emsi.gestioncharite.service.DonateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DonateurServiceImpl implements DonateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final DonRepository donRepository;
    private final ParticipationRepository participationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateProfil(ProfilDonateurRequest request, Donateur donateur) {
        donateur.setNom(request.getNom());
        donateur.setPrenom(request.getPrenom());
        donateur.setEmail(request.getEmail());

        if (StringUtils.hasText(request.getNouveauMotDePasse())) {
            if (!StringUtils.hasText(request.getMotDePasseActuel())) {
                throw new RuntimeException("Veuillez saisir votre mot de passe actuel");
            }
            if (!passwordEncoder.matches(request.getMotDePasseActuel(), donateur.getMotDePasse())) {
                throw new RuntimeException("Mot de passe actuel incorrect");
            }
            if (!request.getNouveauMotDePasse().equals(request.getConfirmMotDePasse())) {
                throw new RuntimeException("Les deux nouveaux mots de passe ne correspondent pas");
            }
            donateur.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        }

        utilisateurRepository.save(donateur);
    }

    @Override
    public long countDons(Donateur donateur) {
        return donRepository.countByDonateur(donateur);
    }

    @Override
    public double sumMontantDons(Donateur donateur) {
        return donRepository.sumMontantByDonateur(donateur);
    }

    @Override
    public long countParticipations(Donateur donateur) {
        return participationRepository.countByDonateurAndStatutNot(donateur, StatutParticipation.ANNULEE);
    }
}

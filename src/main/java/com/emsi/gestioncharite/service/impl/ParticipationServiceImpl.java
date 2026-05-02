package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.entity.Participation;
import com.emsi.gestioncharite.enums.StatutParticipation;
import com.emsi.gestioncharite.enums.TypeAction;
import com.emsi.gestioncharite.repository.ActionChariteRepository;
import com.emsi.gestioncharite.repository.ParticipationRepository;
import com.emsi.gestioncharite.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final ActionChariteRepository actionChariteRepository;

    @Override
    @Transactional
    public void inscrire(int actionId, Donateur donateur) {
        ActionCharite action = actionChariteRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action introuvable"));

        if (action.getTypeAction() != TypeAction.PHYSIQUE) {
            throw new RuntimeException("Cette action n'accepte pas de participations physiques");
        }
        if (participationRepository.existsByActionChariteAndDonateurAndStatutNot(action, donateur, StatutParticipation.ANNULEE)) {
            throw new RuntimeException("Vous êtes déjà inscrit à cette action");
        }
        long nbInscrits = participationRepository.countByActionChariteAndStatutNot(action, StatutParticipation.ANNULEE);
        if (action.getNombrePlacesMax() != null && nbInscrits >= action.getNombrePlacesMax()) {
            throw new RuntimeException("Plus de places disponibles pour cette action");
        }

        Participation participation = new Participation();
        participation.setActionCharite(action);
        participation.setDonateur(donateur);
        participation.setStatut(StatutParticipation.EN_ATTENTE);
        participation.setDateInscription(new Date());
        participationRepository.save(participation);
    }

    @Override
    @Transactional
    public void annuler(int participationId, Donateur donateur) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new RuntimeException("Participation introuvable"));

        if (participation.getDonateur().getId() != donateur.getId()) {
            throw new RuntimeException("Accès refusé");
        }
        if (participation.getStatut() == StatutParticipation.ANNULEE) {
            throw new RuntimeException("Cette participation est déjà annulée");
        }
        if (participation.getStatut() == StatutParticipation.REFUSEE) {
            throw new RuntimeException("Impossible d'annuler une participation refusée");
        }

        participation.setStatut(StatutParticipation.ANNULEE);
        participationRepository.save(participation);
    }

    @Override
    public boolean estInscrit(int actionId, Donateur donateur) {
        ActionCharite action = actionChariteRepository.findById(actionId).orElse(null);
        if (action == null) return false;
        return participationRepository.existsByActionChariteAndDonateurAndStatutNot(action, donateur, StatutParticipation.ANNULEE);
    }

    @Override
    public long getNombreParticipants(int actionId) {
        ActionCharite action = actionChariteRepository.findById(actionId).orElse(null);
        if (action == null) return 0;
        return participationRepository.countByActionChariteAndStatutNot(action, StatutParticipation.ANNULEE);
    }

    @Override
    public Page<Participation> getMesParticipations(Donateur donateur, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("dateInscription").descending());
        return participationRepository.findByDonateurAndStatutNotOrderByDateInscriptionDesc(
                donateur, StatutParticipation.ANNULEE, pageable);
    }
}

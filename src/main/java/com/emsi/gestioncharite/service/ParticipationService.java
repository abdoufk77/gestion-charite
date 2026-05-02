package com.emsi.gestioncharite.service;

import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.entity.Participation;
import org.springframework.data.domain.Page;

public interface ParticipationService {

    void inscrire(int actionId, Donateur donateur);

    void annuler(int participationId, Donateur donateur);

    boolean estInscrit(int actionId, Donateur donateur);

    long getNombreParticipants(int actionId);

    Page<Participation> getMesParticipations(Donateur donateur, int page, int size);
}

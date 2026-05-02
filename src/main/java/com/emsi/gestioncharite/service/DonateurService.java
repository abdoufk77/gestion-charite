package com.emsi.gestioncharite.service;

import com.emsi.gestioncharite.dto.request.ProfilDonateurRequest;
import com.emsi.gestioncharite.entity.Donateur;

public interface DonateurService {

    void updateProfil(ProfilDonateurRequest request, Donateur donateur);

    long countDons(Donateur donateur);

    double sumMontantDons(Donateur donateur);

    long countParticipations(Donateur donateur);
}

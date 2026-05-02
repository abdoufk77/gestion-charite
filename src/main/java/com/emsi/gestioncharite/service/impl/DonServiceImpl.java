package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.Don;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.enums.MethodePaiement;
import com.emsi.gestioncharite.enums.StatutDon;
import com.emsi.gestioncharite.enums.TypeAction;
import com.emsi.gestioncharite.repository.ActionChariteRepository;
import com.emsi.gestioncharite.repository.DonRepository;
import com.emsi.gestioncharite.service.DonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class DonServiceImpl implements DonService {

    private final DonRepository donRepository;
    private final ActionChariteRepository actionChariteRepository;

    @Override
    @Transactional
    public void effectuerDon(int actionId, double montant, MethodePaiement methode, Donateur donateur) {
        ActionCharite action = actionChariteRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action introuvable"));

        if (action.getTypeAction() != TypeAction.FINANCIER) {
            throw new RuntimeException("Cette action n'accepte pas de dons financiers");
        }

        Don don = new Don();
        don.setMontant(montant);
        don.setMethodePaiement(methode);
        don.setDateDon(new Date());
        don.setActionCharite(action);
        don.setDonateur(donateur);
        // Simulation : on confirme directement
        don.setStatut(StatutDon.CONFIRME);
        don.setTransactionId("SIM-" + System.currentTimeMillis());

        donRepository.save(don);
    }

    @Override
    public Page<Don> getHistorique(Donateur donateur, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("dateDon").descending());
        return donRepository.findByDonateurOrderByDateDonDesc(donateur, pageable);
    }

    @Override
    public double getMontantCollecte(ActionCharite action) {
        return donRepository.sumMontantConfirme(action);
    }
}

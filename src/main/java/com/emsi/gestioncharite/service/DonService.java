package com.emsi.gestioncharite.service;

import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.Don;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.enums.MethodePaiement;
import org.springframework.data.domain.Page;

public interface DonService {

    void effectuerDon(int actionId, double montant, MethodePaiement methode, Donateur donateur);

    Page<Don> getHistorique(Donateur donateur, int page, int size);

    double getMontantCollecte(ActionCharite action);
}

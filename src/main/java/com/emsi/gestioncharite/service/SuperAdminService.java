package com.emsi.gestioncharite.service;

import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.StatutOrg;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SuperAdminService {
    List<Organisation> getPendingOrganisations();
    List<Organisation> getAllOrganisations();
    List<Donateur> getAllDonateurs();
    Page<Organisation> searchOrganisations(String search, StatutOrg statut, int page, int size);
    Page<Donateur> searchDonateurs(String search, Boolean enabled, int page, int size);
    Map<Integer, AdminOrganisation> getAdminOrgMap();
    long countActiveOrganisations();
    long countActiveDonateurs();
    void approuver(int organisationId);
    void rejeter(int organisationId);
    void toggleOrganisationAdmin(int orgId);
    void toggleOrganisationStatut(int orgId);
    void toggleDonateur(int donateurId);
}

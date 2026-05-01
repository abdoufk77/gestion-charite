package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.StatutOrg;
import com.emsi.gestioncharite.repository.AdminOrganisationRepository;
import com.emsi.gestioncharite.repository.OrganisationRepository;
import com.emsi.gestioncharite.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final OrganisationRepository organisationRepository;
    private final AdminOrganisationRepository adminOrganisationRepository;

    @Override
    public List<Organisation> getPendingOrganisations() {
        return organisationRepository.findByStatut(StatutOrg.EN_ATTENTE);
    }

    @Override
    @Transactional
    public void approuver(int organisationId) {
        Organisation org = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new RuntimeException("Organisation introuvable"));
        org.setStatut(StatutOrg.ACTIVE);
        organisationRepository.save(org);

        adminOrganisationRepository.findByOrganisation(org).ifPresent(admin -> {
            admin.setEnabled(true);
            adminOrganisationRepository.save(admin);
        });
    }

    @Override
    @Transactional
    public void rejeter(int organisationId) {
        Organisation org = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new RuntimeException("Organisation introuvable"));
        org.setStatut(StatutOrg.REJETEE);
        organisationRepository.save(org);

        adminOrganisationRepository.findByOrganisation(org).ifPresent(admin -> {
            admin.setEnabled(false);
            adminOrganisationRepository.save(admin);
        });
    }
}

package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.StatutOrg;
import com.emsi.gestioncharite.repository.AdminOrganisationRepository;
import com.emsi.gestioncharite.repository.DonateurRepository;
import com.emsi.gestioncharite.repository.OrganisationRepository;
import com.emsi.gestioncharite.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final OrganisationRepository organisationRepository;
    private final AdminOrganisationRepository adminOrganisationRepository;
    private final DonateurRepository donateurRepository;
    private final SessionRegistry sessionRegistry;

    @Override
    public List<Organisation> getPendingOrganisations() {
        return organisationRepository.findByStatut(StatutOrg.EN_ATTENTE);
    }

    @Override
    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }

    @Override
    public List<Donateur> getAllDonateurs() {
        return donateurRepository.findAll();
    }

    @Override
    public Map<Integer, AdminOrganisation> getAdminOrgMap() {
        Map<Integer, AdminOrganisation> map = new HashMap<>();
        adminOrganisationRepository.findAll().forEach(admin -> {
            if (admin.getOrganisation() != null) {
                map.put(admin.getOrganisation().getId(), admin);
            }
        });
        return map;
    }

    @Override
    public Page<Organisation> searchOrganisations(String search, StatutOrg statut, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        String s = StringUtils.hasText(search) ? search.trim() : "";
        return organisationRepository.search(s, statut, pageable);
    }

    @Override
    public Page<Donateur> searchDonateurs(String search, Boolean enabled, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        String s = StringUtils.hasText(search) ? search.trim() : "";
        return donateurRepository.search(s, enabled, pageable);
    }

    @Override
    public long countActiveOrganisations() {
        return organisationRepository.findByStatut(StatutOrg.ACTIVE).size();
    }

    @Override
    public long countActiveDonateurs() {
        return donateurRepository.findAll().stream().filter(d -> d.isEnabled()).count();
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

    @Override
    @Transactional
    public void toggleOrganisationStatut(int orgId) {
        Organisation org = organisationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organisation introuvable"));
        if (org.getStatut() == StatutOrg.ACTIVE) {
            org.setStatut(StatutOrg.SUSPENDUE);
        } else if (org.getStatut() == StatutOrg.SUSPENDUE) {
            org.setStatut(StatutOrg.ACTIVE);
        } else {
            throw new RuntimeException("Seules les organisations actives ou suspendues peuvent être basculées.");
        }
        organisationRepository.save(org);
    }

    @Override
    @Transactional
    public void toggleOrganisationAdmin(int orgId) {
        Organisation org = organisationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organisation introuvable"));
        adminOrganisationRepository.findByOrganisation(org).ifPresent(admin -> {
            boolean wasEnabled = admin.isEnabled();
            admin.setEnabled(!wasEnabled);
            adminOrganisationRepository.save(admin);
            if (wasEnabled) {
                expireUserSessions(admin.getEmail());
            }
        });
    }

    @Override
    @Transactional
    public void toggleDonateur(int donateurId) {
        Donateur donateur = donateurRepository.findById(donateurId)
                .orElseThrow(() -> new RuntimeException("Donateur introuvable"));
        boolean wasEnabled = donateur.isEnabled();
        donateur.setEnabled(!wasEnabled);
        donateurRepository.save(donateur);
        if (wasEnabled) {
            expireUserSessions(donateur.getEmail());
        }
    }

    private void expireUserSessions(String email) {
        sessionRegistry.getAllPrincipals().stream()
                .filter(p -> p instanceof UserDetails && ((UserDetails) p).getUsername().equals(email))
                .flatMap(p -> sessionRegistry.getAllSessions(p, false).stream())
                .forEach(SessionInformation::expireNow);
    }
}

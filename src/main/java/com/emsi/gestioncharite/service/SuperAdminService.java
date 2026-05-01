package com.emsi.gestioncharite.service;

import com.emsi.gestioncharite.entity.Organisation;

import java.util.List;

public interface SuperAdminService {
    List<Organisation> getPendingOrganisations();
    void approuver(int organisationId);
    void rejeter(int organisationId);
}

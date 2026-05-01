package com.emsi.gestioncharite.service;

import com.emsi.gestioncharite.dto.request.OrganisationProfileRequest;
import com.emsi.gestioncharite.entity.Organisation;

public interface OrganisationService {
    void modifierProfil(Organisation organisation, OrganisationProfileRequest request);
}

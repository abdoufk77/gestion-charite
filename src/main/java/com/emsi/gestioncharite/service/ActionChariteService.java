package com.emsi.gestioncharite.service;

import com.emsi.gestioncharite.dto.request.ActionChariteRequest;
import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.enums.Categorie;
import org.springframework.data.domain.Page;

public interface ActionChariteService {
    Page<ActionCharite> getActionsByAdmin(AdminOrganisation admin, int page, int size);
    Page<ActionCharite> search(AdminOrganisation admin, String search, Categorie categorie, int page, int size);
    ActionCharite getActionByIdAndAdmin(int id, AdminOrganisation admin);
    void creer(ActionChariteRequest request, AdminOrganisation admin);
    void modifier(int id, ActionChariteRequest request, AdminOrganisation admin);
    void supprimer(int id, AdminOrganisation admin);
}

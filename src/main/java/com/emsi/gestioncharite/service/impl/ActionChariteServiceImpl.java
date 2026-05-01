package com.emsi.gestioncharite.service.impl;

import com.emsi.gestioncharite.dto.request.ActionChariteRequest;
import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.Categorie;
import com.emsi.gestioncharite.repository.ActionChariteRepository;
import com.emsi.gestioncharite.service.ActionChariteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ActionChariteServiceImpl implements ActionChariteService {

    private final ActionChariteRepository actionChariteRepository;

    @Override
    public Page<ActionCharite> getActionsByAdmin(AdminOrganisation admin, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return actionChariteRepository.findByOrganisation(admin.getOrganisation(), pageable);
    }

    @Override
    public Page<ActionCharite> search(AdminOrganisation admin, String search, Categorie categorie, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Organisation org = admin.getOrganisation();
        boolean hasSearch = StringUtils.hasText(search);
        boolean hasCat = categorie != null;

        if (hasSearch && hasCat) {
            return actionChariteRepository.findByOrganisationAndCategorieAndKeyword(org, categorie, search.trim(), pageable);
        } else if (hasSearch) {
            return actionChariteRepository.findByOrganisationAndKeyword(org, search.trim(), pageable);
        } else if (hasCat) {
            return actionChariteRepository.findByOrganisationAndCategorie(org, categorie, pageable);
        } else {
            return actionChariteRepository.findByOrganisation(org, pageable);
        }
    }

    @Override
    public ActionCharite getActionByIdAndAdmin(int id, AdminOrganisation admin) {
        ActionCharite action = actionChariteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action introuvable"));
        if (action.getOrganisation() == null || action.getOrganisation().getId() != admin.getOrganisation().getId()) {
            throw new RuntimeException("Accès refusé");
        }
        return action;
    }

    @Override
    @Transactional
    public void creer(ActionChariteRequest request, AdminOrganisation admin) {
        ActionCharite action = new ActionCharite();
        action.setTitre(request.getTitre());
        action.setDescription(request.getDescription());
        action.setDateDebut(request.getDateDebut());
        action.setDateFin(request.getDateFin());
        action.setCategorie(request.getCategorie());
        action.setOrganisation(admin.getOrganisation());
        actionChariteRepository.save(action);
    }

    @Override
    @Transactional
    public void modifier(int id, ActionChariteRequest request, AdminOrganisation admin) {
        ActionCharite action = getActionByIdAndAdmin(id, admin);
        action.setTitre(request.getTitre());
        action.setDescription(request.getDescription());
        action.setDateDebut(request.getDateDebut());
        action.setDateFin(request.getDateFin());
        action.setCategorie(request.getCategorie());
        actionChariteRepository.save(action);
    }

    @Override
    @Transactional
    public void supprimer(int id, AdminOrganisation admin) {
        ActionCharite action = getActionByIdAndAdmin(id, admin);
        actionChariteRepository.delete(action);
    }
}

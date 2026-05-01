package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.StatutOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {
    List<Organisation> findByStatut(StatutOrg statut);
}

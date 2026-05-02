package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.Donateur;
import com.emsi.gestioncharite.entity.Participation;
import com.emsi.gestioncharite.enums.StatutParticipation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

    // Vérifie si le donateur a une inscription active (non annulée) pour cette action
    boolean existsByActionChariteAndDonateurAndStatutNot(ActionCharite actionCharite,
                                                         Donateur donateur,
                                                         StatutParticipation statut);

    // Compte uniquement les participants actifs (exclut les annulées)
    long countByActionChariteAndStatutNot(ActionCharite actionCharite, StatutParticipation statut);

    // Liste paginée des participations actives d'un donateur (exclut les annulées)
    Page<Participation> findByDonateurAndStatutNotOrderByDateInscriptionDesc(Donateur donateur,
                                                                              StatutParticipation statut,
                                                                              Pageable pageable);

    long countByDonateurAndStatutNot(Donateur donateur, StatutParticipation statut);

    List<Participation> findByActionChariteOrderByDateInscriptionDesc(ActionCharite actionCharite);

    List<Participation> findByActionChariteAndStatutOrderByDateInscriptionDesc(ActionCharite actionCharite, StatutParticipation statut);

    long countByActionChariteAndStatut(ActionCharite actionCharite, StatutParticipation statut);

    @Query("SELECT COUNT(p) FROM Participation p WHERE p.actionCharite.organisation = :org AND p.statut = :statut")
    long countByOrganisationAndStatut(@Param("org") com.emsi.gestioncharite.entity.Organisation org,
                                      @Param("statut") StatutParticipation statut);
}

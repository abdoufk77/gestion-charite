package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.StatutOrg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {

    List<Organisation> findByStatut(StatutOrg statut);

    @Query("""
        SELECT o FROM Organisation o
        WHERE (:statut IS NULL OR o.statut = :statut)
          AND (:search IS NULL OR :search = ''
               OR LOWER(o.nom) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(o.contactPrincipal) LIKE LOWER(CONCAT('%', :search, '%')))
        """)
    Page<Organisation> search(@Param("search") String search,
                              @Param("statut") StatutOrg statut,
                              Pageable pageable);
}

package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.Organisation;
import com.emsi.gestioncharite.enums.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionChariteRepository extends JpaRepository<ActionCharite, Integer> {

    List<ActionCharite> findByOrganisation(Organisation organisation);

    Page<ActionCharite> findByOrganisation(Organisation organisation, Pageable pageable);

    Page<ActionCharite> findByOrganisationAndCategorie(Organisation organisation,
                                                        Categorie categorie,
                                                        Pageable pageable);

    @Query("""
        SELECT a FROM ActionCharite a
        WHERE a.organisation = :org
          AND (LOWER(a.titre) LIKE LOWER(CONCAT('%', :kw, '%'))
               OR LOWER(a.description) LIKE LOWER(CONCAT('%', :kw, '%')))
        """)
    Page<ActionCharite> findByOrganisationAndKeyword(@Param("org") Organisation org,
                                                     @Param("kw") String kw,
                                                     Pageable pageable);

    @Query("""
        SELECT a FROM ActionCharite a
        WHERE a.organisation = :org
          AND a.categorie = :cat
          AND (LOWER(a.titre) LIKE LOWER(CONCAT('%', :kw, '%'))
               OR LOWER(a.description) LIKE LOWER(CONCAT('%', :kw, '%')))
        """)
    Page<ActionCharite> findByOrganisationAndCategorieAndKeyword(@Param("org") Organisation org,
                                                                  @Param("cat") Categorie cat,
                                                                  @Param("kw") String kw,
                                                                  Pageable pageable);
}

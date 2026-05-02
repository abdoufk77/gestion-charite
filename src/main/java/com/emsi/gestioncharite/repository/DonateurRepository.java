package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.Donateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonateurRepository extends JpaRepository<Donateur, Integer> {

    @Query("""
        SELECT d FROM Donateur d
        WHERE (:enabled IS NULL OR d.enabled = :enabled)
          AND (:search IS NULL OR :search = ''
               OR LOWER(d.nom) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(d.prenom) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(d.email) LIKE LOWER(CONCAT('%', :search, '%')))
        """)
    Page<Donateur> search(@Param("search") String search,
                          @Param("enabled") Boolean enabled,
                          Pageable pageable);
}

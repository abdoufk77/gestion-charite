package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.ActionCharite;
import com.emsi.gestioncharite.entity.Don;
import com.emsi.gestioncharite.entity.Donateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonRepository extends JpaRepository<Don, Integer> {

    Page<Don> findByDonateurOrderByDateDonDesc(Donateur donateur, Pageable pageable);

    @Query("SELECT COALESCE(SUM(d.montant), 0.0) FROM Don d WHERE d.actionCharite = :action AND d.statut = 'CONFIRME'")
    double sumMontantConfirme(@Param("action") ActionCharite action);

    long countByDonateur(Donateur donateur);

    @Query("SELECT COALESCE(SUM(d.montant), 0.0) FROM Don d WHERE d.donateur = :donateur AND d.statut = 'CONFIRME'")
    double sumMontantByDonateur(@Param("donateur") Donateur donateur);

    List<Don> findByActionChariteOrderByDateDonDesc(ActionCharite action);

    @Query("SELECT COALESCE(SUM(d.montant), 0.0) FROM Don d WHERE d.actionCharite.organisation = :org AND d.statut = 'CONFIRME'")
    double sumMontantByOrganisation(@Param("org") com.emsi.gestioncharite.entity.Organisation org);
}

package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.AdminOrganisation;
import com.emsi.gestioncharite.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminOrganisationRepository extends JpaRepository<AdminOrganisation, Integer> {
    Optional<AdminOrganisation> findByOrganisation(Organisation organisation);

    @Query("SELECT a FROM AdminOrganisation a LEFT JOIN FETCH a.organisation WHERE a.email = :email")
    Optional<AdminOrganisation> findByEmailWithOrganisation(@Param("email") String email);
}

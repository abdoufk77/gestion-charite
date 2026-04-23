package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.AdminOrganisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminOrganisationRepository extends JpaRepository<AdminOrganisation, Integer> {
}

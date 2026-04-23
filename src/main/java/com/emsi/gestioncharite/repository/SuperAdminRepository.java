package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Integer> {
}

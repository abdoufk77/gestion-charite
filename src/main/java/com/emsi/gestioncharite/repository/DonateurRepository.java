package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.Donateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonateurRepository extends JpaRepository<Donateur, Integer> {
}

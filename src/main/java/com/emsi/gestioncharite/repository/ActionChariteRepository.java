package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.ActionCharite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionChariteRepository extends JpaRepository<ActionCharite, Integer> {
}

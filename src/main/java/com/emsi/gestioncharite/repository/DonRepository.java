package com.emsi.gestioncharite.repository;

import com.emsi.gestioncharite.entity.Don;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonRepository extends JpaRepository<Don, Integer> {
}

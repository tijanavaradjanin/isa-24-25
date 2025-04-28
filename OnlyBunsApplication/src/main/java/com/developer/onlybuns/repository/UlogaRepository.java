package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Uloga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UlogaRepository extends JpaRepository<Uloga, Long> {
    Uloga findByNaziv(String naziv);
}

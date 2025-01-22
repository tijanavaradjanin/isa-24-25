package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Objava;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ObjavaRepository extends JpaRepository<Objava, Integer> {
    @Query("SELECT o FROM Objava o WHERE o.registrovaniKorisnik.id = :korisnikId")
    List<Objava> findByKorisnikId(Integer korisnikId);
}

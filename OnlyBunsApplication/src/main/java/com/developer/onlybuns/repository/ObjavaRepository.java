package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.Pracenje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ObjavaRepository extends JpaRepository<Objava, Integer> {
    @Query("SELECT o FROM Objava o WHERE o.registrovaniKorisnik.id = :korisnikId")
    List<Objava> findByKorisnikId(Integer korisnikId);

    @Query("SELECT o FROM Objava o ORDER BY o.vremeKreiranja DESC")
    List<Objava> findAllObjave();

    @Query("select o from Objava o where o.registrovaniKorisnik.id=:korisnikId and o.vremeKreiranja > :sevenDaysAgo")
    List<Objava> findByKorisnikIdAndVremeKreiranja(Integer korisnikId, @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}

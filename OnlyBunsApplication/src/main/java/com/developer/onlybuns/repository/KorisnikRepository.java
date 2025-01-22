package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {
    Korisnik findByEmailAndPassword(String email, String password);
    @Query("SELECT email FROM Korisnik")
    List<String> findAllEmails();
    Optional<Korisnik> findByKorisnickoIme(String korisnickoIme);
    boolean existsByKorisnickoIme(String korisnickoIme);
    boolean existsByEmail(String email);
    Optional<Korisnik> findByEmail(String email);
}
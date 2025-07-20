package com.developer.onlybuns.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrovaniKorisnikRepository extends JpaRepository<RegistrovaniKorisnik, Integer> {

    RegistrovaniKorisnik findByEmailAndPassword(String email, String password);

    @Query("SELECT email FROM RegistrovaniKorisnik")
    List<String> findAllEmails();

    RegistrovaniKorisnik findByKorisnickoIme(String korisnickoIme);

    Optional<RegistrovaniKorisnik> findByEmail(String email);

    @Query("SELECT rk FROM RegistrovaniKorisnik rk WHERE rk.lastLogin < :sevenDaysAgo")
    List<RegistrovaniKorisnik> findByLastLoginBefore(@Param("sevenDaysAgo") Timestamp sevenDaysAgo);

    void deleteByKorisnickoIme(String korisnickoIme);

}

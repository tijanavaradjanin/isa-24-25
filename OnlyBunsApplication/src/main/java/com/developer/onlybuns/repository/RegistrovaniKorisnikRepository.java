package com.developer.onlybuns.repository;

import java.util.List;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrovaniKorisnikRepository extends JpaRepository<RegistrovaniKorisnik, Integer> {
    @Query("SELECT email FROM RegistrovaniKorisnik")
    List<String> findAllEmails();
    RegistrovaniKorisnik findByEmailAndPassword(String email, String password);
}

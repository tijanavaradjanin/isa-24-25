package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LajkRepository extends JpaRepository<Lajk, Integer> {

    @Query("select l from Lajk l where l.objava.id=:objavaId and l.vremeLajkovanja > :sevenDaysAgo")
    List<Lajk> findByObjavaIdAndVremeLajkovanja(Integer objavaId, @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

    Optional<Lajk> findByRegistrovaniKorisnikAndObjava(RegistrovaniKorisnik korisnik, Objava objava);

}

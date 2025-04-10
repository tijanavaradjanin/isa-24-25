package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Pracenje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PracenjeRepository extends JpaRepository<Pracenje, Integer> {

    @Query("select p from Pracenje p where p.pratilac.id=:pracenjeId")
    List<Pracenje> findByPracenjeId(Integer pracenjeId);

    @Query("select p from Pracenje p where p.zapraceni.id=:zapraceniId and p.vremeZapracivanja > :sevenDaysAgo")
    List<Pracenje> findByZapraceniIdAndDatumPracenja(Integer zapraceniId,  @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

    boolean existsByPratilacIdAndZapraceniId(Integer pratilacId, Integer zapraceniId);

    @Query("select p from Pracenje p where p.pratilac.id=:pratilacId and p.zapraceni.id=:zapraceniId")
    Pracenje findByPratilacIdAndZapraceniId(Integer pratilacId, Integer zapraceniId);
}

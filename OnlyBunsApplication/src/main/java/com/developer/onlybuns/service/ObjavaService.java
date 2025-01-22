package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ObjavaService {
    //List<Objava> findAllObjava();

    //Objava findById(Integer id);

    List<Objava> findByKorisnikId(Integer korisnikId);

    Optional<Objava> getById(Integer id);

    void saveObjava(Objava objava);

    Objava updateObjava(Objava objava);

    void deleteObjava(Integer id);

    List<Lajk> getAllLajkovi(Integer id);

    List<Komentar> getAllKomentari(Integer id);

    // List<Objava> findAllObjavaByUsername(String username);

   //  public int brojKomentaraObjave(String username, LocalDateTime fromDate);         //za statistiku

  //  public int brojLajkovaObjave(String username, LocalDateTime fromDate);           //za statistiku
}

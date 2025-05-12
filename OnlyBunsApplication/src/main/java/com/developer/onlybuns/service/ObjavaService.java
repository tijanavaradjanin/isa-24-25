package com.developer.onlybuns.service;

import com.developer.onlybuns.dto.ObjavaDTO;
import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ObjavaService {
    //List<Objava> findAllObjava();

    Optional<Objava> getById(Integer id);

    List<Objava> getAllObjave();

    List<Objava> findByKorisnikId(Integer korisnikId);

    void saveObjava(Objava objava);

    Objava updateObjava(Objava objava);

    void deleteObjava(Integer id);

    List<Lajk> getAllLajkovi(Integer id);

    List<Komentar> getAllKomentari(Integer id);

    double[] validateKorisnikLocation(String grad, String drzava);

    double[] validateLocation(String grad, String drzava, String ulica, Integer broj);

    public List<ObjavaDTO> findNearbyPosts(RegistrovaniKorisnik korisnik);

    // List<Objava> findAllObjavaByUsername(String username);

   //  public int brojKomentaraObjave(String username, LocalDateTime fromDate);         //za statistiku

  //  public int brojLajkovaObjave(String username, LocalDateTime fromDate);           //za statistiku
}

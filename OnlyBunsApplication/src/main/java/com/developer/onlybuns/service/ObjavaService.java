package com.developer.onlybuns.service;

import com.developer.onlybuns.dto.ObjavaDTO;
import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;

import java.util.List;
import java.util.Optional;

public interface ObjavaService {

    Optional<Objava> getById(Integer id);

    List<Objava> getAllObjave();

    List<Objava> findByKorisnikId(Integer korisnikId);

    void saveObjava(Objava objava);

    List<Lajk> getAllLajkovi(Integer id);

    List<Komentar> getAllKomentari(Integer id);

    double[] validateLocation(String grad, String drzava, String ulica, String broj);

    public List<ObjavaDTO> findNearbyPosts(RegistrovaniKorisnik korisnik);

}

package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;

import java.util.Optional;

public interface LajkService {

    void saveLajk(Lajk lajk);

    Lajk findByKorisnikAndObjava(RegistrovaniKorisnik korisnik, Objava objava);

}

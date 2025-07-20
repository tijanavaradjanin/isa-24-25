package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.Korisnik;
import java.util.Optional;

public interface KorisnikService {

    Optional<Korisnik> findByEmail(String email);

    Optional<Korisnik> findByKorisnickoIme(String korisnickoIme);

}

package com.developer.onlybuns.service;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;

import java.util.List;
import java.util.Optional;

public interface RegistrovaniKorisnikService {

    RegistrovaniKorisnik findByKorisnickoIme(String korisnickoIme);

    RegistrovaniKorisnik saveRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik);

    RegistrovaniKorisnik updateRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik);

    List<String> getAllEmails();

    Optional<RegistrovaniKorisnik> findByEmail(String email);

}

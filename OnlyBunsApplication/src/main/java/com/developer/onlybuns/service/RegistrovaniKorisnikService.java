package com.developer.onlybuns.service;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;

import java.util.List;
import java.util.Optional;

public interface RegistrovaniKorisnikService {
    Optional<RegistrovaniKorisnik> findById(Integer id);
    RegistrovaniKorisnik saveRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik);
    RegistrovaniKorisnik updateRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik);
    RegistrovaniKorisnik proveriKredencijale(String email, String password);
    void deleteRegistrovaniKorisnik(Integer id);
    List<RegistrovaniKorisnik> findAllRegistrovaniKorisnik();
    List<String> getAllEmails();
}

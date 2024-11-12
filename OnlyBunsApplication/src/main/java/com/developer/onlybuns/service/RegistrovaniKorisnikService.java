package com.developer.onlybuns.service;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;

import java.util.List;
import java.util.Optional;

public interface RegistrovaniKorisnikService {
    List<RegistrovaniKorisnik> findAllRegistrovaniKorisnik();
    Optional<RegistrovaniKorisnik> findById(Integer id);
    RegistrovaniKorisnik saveRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik);
    RegistrovaniKorisnik updateRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik);
    void deleteRegistrovaniKorisnik(Integer id);
    List<String> getAllEmails();
    RegistrovaniKorisnik proveriKorisnika(String email, String password);

/*    Using Request for Save and Update RegistrovaniKorisnik
    RegistrovaniKorisnikResponse saveRegistrovaniKorisnik(RegistrovaniKorisnikRequest employeeRequest);
    RegistrovaniKorisnikResponse updateRegistrovaniKorisnik(RegistrovaniKorisnikRequest employeeRequest, Integer id);

*/
}

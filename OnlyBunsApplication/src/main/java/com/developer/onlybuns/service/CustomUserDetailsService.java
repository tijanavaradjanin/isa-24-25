package com.developer.onlybuns.service;
import java.util.Optional;
import com.developer.onlybuns.entity.Korisnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private RegistrovaniKorisnikService registrovaniKorisnikService;

    @Autowired
    private KorisnikService korisnikService;

    public CustomUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String korisnickoIme) throws UsernameNotFoundException {
        if (korisnickoIme.contains("@")) {
            Optional<Korisnik> korisnik = korisnikService.findByEmail(korisnickoIme);
            if (!korisnik.isPresent()) {
                throw new UsernameNotFoundException("Korisnik sa mejlom: " + korisnickoIme + " nije pronađen.");
            } else {
                System.out.println("Pronađen korisnik.");
                System.out.println(korisnik.get().getEmail());
                System.out.println(korisnik.get().getPassword());
                System.out.println(korisnik.get().getUloga().getNaziv());
                System.out.println("Proveravam lozinku...");
                return korisnik.get();
            }
        } else {
            // Ako korisničko ime nije email (npr. korisničko ime)
            Optional<Korisnik> korisnik = korisnikService.findByKorisnickoIme(korisnickoIme);
            if (!korisnik.isPresent()) {
                throw new UsernameNotFoundException("Korisnik sa korisničkim imenom: " + korisnickoIme + " nije pronađen.");
            } else {
                return korisnik.get();
            }
        }
    }

    public UserDetails loadUserByKorisnickoIme (String korisnickoIme)  throws UsernameNotFoundException {
                Optional<Korisnik> korisnik = korisnikService.findByKorisnickoIme(korisnickoIme);
                if (!korisnik.isPresent()) {
                    throw new UsernameNotFoundException("Korisnik sa korisnickim imenom: " + korisnickoIme + "nije pronadjen.");
                } else {
                    System.out.println("Pronadjen korisnik.");
                    System.out.println(korisnik.get().getEmail());
                    System.out.println(korisnik.get().getPassword());
                    return korisnik.get();
                }
            }
}



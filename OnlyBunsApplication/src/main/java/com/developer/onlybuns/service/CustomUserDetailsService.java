package com.developer.onlybuns.service;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.util.ArrayList;
import java.util.Optional;

import com.developer.onlybuns.entity.Korisnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private RegistrovaniKorisnikService registrovaniKorisnikService;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public CustomUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String korisnickoIme)  throws UsernameNotFoundException {
        if(korisnickoIme.contains("@")) {
            Optional<Korisnik> korisnik = korisnikService.findByEmail(korisnickoIme);
            if (!korisnik.isPresent()) {
                throw new UsernameNotFoundException("Korisnik sa mejlom: " + korisnickoIme + "nije pronadjen.");
            } else {
                System.out.println("Pronadjen korisnik.");
                System.out.println(korisnik.get().getEmail());
                System.out.println(korisnik.get().getPassword());
                System.out.println("Proveravam lozinku...");
                System.out.println(passwordEncoder.encode(korisnik.get().getPassword()));
                return new User (korisnik.get().getKorisnickoIme(), passwordEncoder.encode(korisnik.get().getPassword()), new ArrayList<>());
            }
        } else {
            Optional<Korisnik> korisnik=korisnikService.findByKorisnickoIme(korisnickoIme);
            if (!korisnik.isPresent()) {
                throw new UsernameNotFoundException("Korisnik sa korisnickim imenom: " + korisnickoIme + "nije pronadjen.");
            } else {
                System.out.println("Pronadjen korisnik.");
                System.out.println(korisnik.get().getEmail());
                System.out.println(korisnik.get().getPassword());
                System.out.println("Proveravam lozinku...");
                System.out.println(passwordEncoder.encode(korisnik.get().getPassword()));
                return new User (korisnik.get().getKorisnickoIme(), passwordEncoder.encode(korisnik.get().getPassword()), new ArrayList<>());
            }
        }
    }

    public Optional<Korisnik> loadUserByKorisnickoIme(String korisnickoIme)  throws UsernameNotFoundException {
        Optional<Korisnik> korisnik = korisnikService.findByKorisnickoIme(korisnickoIme);
        if (!korisnik.isPresent()) {
            throw new UsernameNotFoundException("Korisnik sa korisnickim imenom: " + korisnickoIme + "nije pronadjen.");
        } else {
            System.out.println("Pronadjen korisnik.");
            System.out.println(korisnik.get().getEmail());
            System.out.println(korisnik.get().getPassword());
            return korisnik;
        }
    }
}

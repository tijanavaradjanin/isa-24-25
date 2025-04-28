package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.entity.Uloga;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import com.developer.onlybuns.repository.UlogaRepository;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrovaniKorisnikImpl implements RegistrovaniKorisnikService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private final RegistrovaniKorisnikRepository registrovaniKorisnikRepository;

    @Autowired
    private final UlogaRepository ulogaRepository;

    public RegistrovaniKorisnikImpl(RegistrovaniKorisnikRepository registrovaniKorisnikRepository, UlogaRepository ulogaRepository) {
        this.registrovaniKorisnikRepository = registrovaniKorisnikRepository;
        this.ulogaRepository=ulogaRepository;
    }

    @Override
    public Optional<RegistrovaniKorisnik> findById(Integer id) {
        return registrovaniKorisnikRepository.findById(id);
    }

    @Override
    public Optional<RegistrovaniKorisnik> findByEmail(String email) {
        return registrovaniKorisnikRepository.findByEmail(email);
    }

    @Override
    public RegistrovaniKorisnik findByKorisnickoIme(String korisnickoIme) {
        return registrovaniKorisnikRepository.findByKorisnickoIme(korisnickoIme);
    }

    @Override
    public RegistrovaniKorisnik saveRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnikEntity) {
            Timestamp now = new Timestamp(new Date().getTime());
            registrovaniKorisnikEntity.setLastPasswordResetDate(now);
            registrovaniKorisnikEntity.setEnabled(true);
            Uloga korisnikUloga = ulogaRepository.findByNaziv("KORISNIK");
            registrovaniKorisnikEntity.setUloga(korisnikUloga);
            registrovaniKorisnikEntity.setPassword(passwordEncoder.encode(registrovaniKorisnikEntity.getPassword()));
            return registrovaniKorisnikRepository.save(registrovaniKorisnikEntity);
    }

    @Override
    public RegistrovaniKorisnik updateRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnikEntity) {
        return registrovaniKorisnikRepository.save(registrovaniKorisnikEntity);
    }

    @Override
    public void deleteRegistrovaniKorisnik(Integer id) {
        registrovaniKorisnikRepository.deleteById(id);
    }

    @Override
    public RegistrovaniKorisnik proveriKredencijale(String email, String password) {
        RegistrovaniKorisnik korisnik = registrovaniKorisnikRepository.findByEmailAndPassword(email, password);
        return korisnik;
    }

    @Override
    public List<RegistrovaniKorisnik> findAllRegistrovaniKorisnik() {
        return registrovaniKorisnikRepository.findAll();
    }

    @Override
    public List<String> getAllEmails() {
        return registrovaniKorisnikRepository.findAllEmails();
    }
    
}

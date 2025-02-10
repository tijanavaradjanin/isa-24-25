package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import com.developer.onlybuns.entity.Uloga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrovaniKorisnikImpl implements RegistrovaniKorisnikService {

    @Autowired
    private final RegistrovaniKorisnikRepository registrovaniKorisnikRepository;

    public RegistrovaniKorisnikImpl(RegistrovaniKorisnikRepository registrovaniKorisnikRepository) {
        this.registrovaniKorisnikRepository = registrovaniKorisnikRepository;
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
        //to do:
        //za grad i drzavu da ne sme biti broj, za ime i prezime ne sme biti broj ili znakovi
        if(registrovaniKorisnikRepository.existsByEmail(registrovaniKorisnikEntity.getEmail())) {
            throw new RuntimeException("Email zauzet");
        }
        if(registrovaniKorisnikRepository.existsByKorisnickoIme(registrovaniKorisnikEntity.getKorisnickoIme())) {
            throw new RuntimeException("Korisnicko ime zauzeto");
        }
        if (registrovaniKorisnikEntity.getGrad().isEmpty()) {
            throw new IllegalArgumentException("Grad je obavezan.");
        }
        if (!registrovaniKorisnikEntity.getGrad().matches("^[a-zA-Z ]+$")) {
            throw new RuntimeException("Naziv grada ne sme sadržati brojeve ili specijalne znakove");
        }
        if (!registrovaniKorisnikEntity.getDrzava().matches("^[a-zA-Z ]+$")) {
            throw new RuntimeException("Naziv države ne sme sadržati brojeve ili specijalne znakove");
        }
        if (!registrovaniKorisnikEntity.getBroj().matches("^[0-9]{10}$")) {
            throw new RuntimeException("Broj telefona mora sadržati tačno 10 cifara");
        }
        try {
            registrovaniKorisnikEntity.setUloga(Uloga.KORISNIK);
            return registrovaniKorisnikRepository.save(registrovaniKorisnikEntity);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Duplicate entry detected", e);
        }
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

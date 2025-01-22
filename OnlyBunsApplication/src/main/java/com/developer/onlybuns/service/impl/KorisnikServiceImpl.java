package com.developer.onlybuns.service.impl;
import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.repository.KorisnikRepository;
import com.developer.onlybuns.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KorisnikServiceImpl implements KorisnikService {

    @Autowired
    private final KorisnikRepository korisnikRepository;

    public KorisnikServiceImpl(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }

    @Override
    public Optional<Korisnik> findByEmail(String email) {
        return korisnikRepository.findByEmail(email);
    }
    @Override
    public Optional<Korisnik> findByKorisnickoIme(String korisnickoIme) {
        return korisnikRepository.findByKorisnickoIme(korisnickoIme);
    }

}

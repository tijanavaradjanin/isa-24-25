package com.developer.onlybuns.entity;


import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="registrovaniKorisnik")
public class RegistrovaniKorisnik extends Korisnik implements UserDetails {
    @Column(name="info", nullable = false)
    private String info;

    public Map<String, Object> getAdditionalClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("info", getInfo());
        return claims;
    }

    public RegistrovaniKorisnik(String email, String password, String ime, String prezime, String korisnickoIme, String grad, String drzava, String broj, String info) {
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.grad = grad;
        this.drzava = drzava;
        this.broj = broj;
        this.info = info;
    }

    public RegistrovaniKorisnik() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

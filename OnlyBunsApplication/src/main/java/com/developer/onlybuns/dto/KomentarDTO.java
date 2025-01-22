package com.developer.onlybuns.dto;

import java.time.LocalDateTime;

public class KomentarDTO { private Integer id;

    private String sadrzaj;

    private String korisnickoIme;

    private LocalDateTime vremeKomentarisanja;

    private Integer idObjave;

    public KomentarDTO() {
    }

    public KomentarDTO(Integer id, String sadrzaj, String korisnickoIme, LocalDateTime vremeKomentarisanja, Integer idObjave) {
        this.id = id;
        this.sadrzaj = sadrzaj;
        this.korisnickoIme = korisnickoIme;
        this.vremeKomentarisanja = vremeKomentarisanja;
        this.idObjave = idObjave;
    }

    public Integer getIdObjave() {
        return idObjave;
    }

    public void setIdObjave(Integer idObjave) {
        this.idObjave = idObjave;
    }

    public LocalDateTime getVremeKomentarisanja() {
        return vremeKomentarisanja;
    }

    public void setVremeKomentarisanja(LocalDateTime vremeKomentarisanja) {
        this.vremeKomentarisanja = vremeKomentarisanja;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

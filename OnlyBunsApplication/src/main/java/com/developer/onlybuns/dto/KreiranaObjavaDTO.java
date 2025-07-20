package com.developer.onlybuns.dto;

import java.time.LocalDateTime;

public class KreiranaObjavaDTO {

    private String opis;

    private String slika;

    private Double latituda;

    private Double longituda;

    private LocalDateTime vremeKreiranja;

    private String korisnickoIme;

    public KreiranaObjavaDTO() {
    }

    public KreiranaObjavaDTO(String opis, String slika, Double latituda, Double longituda, LocalDateTime vremeKreiranja, String korisnickoIme) {
        this.opis = opis;
        this.slika = slika;
        this.latituda = latituda;
        this.longituda = longituda;
        this.vremeKreiranja = vremeKreiranja;
        this.korisnickoIme = korisnickoIme;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public Double getLatituda() {
        return latituda;
    }

    public void setLatituda(Double latituda) {
        this.latituda = latituda;
    }

    public Double getLongituda() {
        return longituda;
    }

    public void setLongituda(Double longituda) {
        this.longituda = longituda;
    }

    public LocalDateTime getVremeKreiranja() {
        return vremeKreiranja;
    }

    public void setVremeKreiranja(LocalDateTime vremeKreiranja) {
        this.vremeKreiranja = vremeKreiranja;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }
}

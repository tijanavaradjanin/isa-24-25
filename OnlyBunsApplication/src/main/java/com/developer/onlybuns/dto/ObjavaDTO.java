package com.developer.onlybuns.dto;

import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Lajk;

import java.time.LocalDateTime;
import java.util.List;

public class ObjavaDTO {
    private Integer id;

    private String opis;

    private String slika;

    private Double latituda;

    private Double longituda;

    private LocalDateTime vremeKreiranja;

    private List<Komentar> komentari;

    private List<Lajk> lajkovi;

    private String korisnickoIme;

    private Integer brojLajkova;                          //za prikaz i statistiku

    private Integer brojKomentara;                       //za prikaz i statistiku

    public ObjavaDTO() {
    }

    public ObjavaDTO(Integer id, String opis, String slika, Double latituda, Double longituda, LocalDateTime vremeKreiranja, List<Komentar> komentari, List<Lajk> lajkovi, String korisnickoIme, Integer brojLajkova, Integer brojKomentara) {
        this.id = id;
        this.opis = opis;
        this.slika = slika;
        this.latituda = latituda;
        this.longituda = longituda;
        this.vremeKreiranja = vremeKreiranja;
        this.komentari = komentari;
        this.lajkovi = lajkovi;
        this.korisnickoIme = korisnickoIme;
        this.brojLajkova = brojLajkova;
        this.brojKomentara = brojKomentara;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Komentar> getKomentari() {
        return komentari;
    }

    public void setKomentari(List<Komentar> komentari) {
        this.komentari = komentari;
    }

    public List<Lajk> getLajkovi() {
        return lajkovi;
    }

    public void setLajkovi(List<Lajk> lajkovi) {
        this.lajkovi = lajkovi;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public Integer getBrojLajkova() {
        return brojLajkova;
    }

    public void setBrojLajkova(Integer brojLajkova) {
        this.brojLajkova = brojLajkova;
    }

    public Integer getBrojKomentara() {
        return brojKomentara;
    }

    public void setBrojKomentara(Integer brojKomentara) {
        this.brojKomentara = brojKomentara;
    }
}

package com.developer.onlybuns.dto;

public class KorisnikProfilDTO {
    private String korisnickoIme;
    private String ime;
    private String prezime;
    private String adresa;
    private String broj;
    private String email;
    private String info;

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public KorisnikProfilDTO(String korisnickoIme, String ime, String prezime, String adresa, String broj, String email, String info) {
        this.korisnickoIme = korisnickoIme;
        this.ime = ime;
        this.prezime = prezime;
        this.adresa=adresa;
        this.broj = broj;
        this.email = email;
        this.info = info;
    }

    public KorisnikProfilDTO() {
    }
}

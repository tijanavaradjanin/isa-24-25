package com.developer.onlybuns.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Obavezno polje")
    private String korisnickoIme;

    @NotBlank(message = "Obavezno polje")
    @Size(min = 6, message = "Lozinka mora imati min. 6 karaktera")
    private String password;

    @NotBlank(message = "Obavezno polje")
    private String potvrdaLozinke;

    @NotBlank(message = "Obavezno polje")
    private String ime;

    @NotBlank(message = "Obavezno polje")
    private String prezime;

    @NotBlank(message = "Obavezno polje")
    @Email(message = "Neispravan format email adrese")
    private String email;

    @NotBlank(message = "Obavezno polje")
    private String grad;

    @NotBlank(message = "Obavezno polje")
    private String drzava;

    private String broj;

    private String info;

    public UserRequest() {}

    public UserRequest(String korisnickoIme, String lozinka, String potvrdaLozinke, String ime, String prezime, String email, String grad, String drzava, String broj, String info) {
        this.korisnickoIme = korisnickoIme;
        this.password = lozinka;
        this.potvrdaLozinke = potvrdaLozinke;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.grad = grad;
        this.drzava = drzava;
        this.broj = broj;
        this.info = info;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String lozinka) {
        this.password = lozinka;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getPotvrdaLozinke() {
        return potvrdaLozinke;
    }

    public void setPotvrdaLozinke(String potvrdaLozinke) {
        this.potvrdaLozinke = potvrdaLozinke;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

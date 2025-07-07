package com.developer.onlybuns.dto;

import com.developer.onlybuns.service.NotBlankIfPresent;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProfile {

    @NotBlankIfPresent(message = "Korisničko ime ne može biti prazno ako se menja.")
    private String korisnickoIme;

    private String password;

    @Size(min = 6, message = "Nova lozinka mora imati min. 6 karaktera.")
    private String novaLozinka;

    private String potvrdaNoveLozinke;

    @NotBlankIfPresent(message = "Ime ne može biti prazno ako se menja.")
    private String ime;

    @NotBlankIfPresent(message = "Prezime ne može biti prazno ako se menja.")
    private String prezime;

    @NotBlankIfPresent(message = "Grad ne može biti prazan ako se menja.")
    private String grad;

    @NotBlankIfPresent(message = "Drzava ne može biti prazna ako se menja.")
    private String drzava;

    @NotBlankIfPresent(message = "Ulica ne može biti prazna ako se menja.")
    private String ulica;

    @Pattern(regexp = "^[0-9a-zA-Z/\\- ]{0,15}$", message = "Broj adrese nije validan.")
    private String brojKuce;

    private String broj;

    private String info;

    public UpdateProfile(String info, String broj, String drzava, String grad, String ulica, String brojKuce, String prezime, String ime, String potvrdaNoveLozinke, String novaLozinka, String password, String korisnickoIme) {
        this.info = info;
        this.broj = broj;
        this.drzava = drzava;
        this.grad = grad;
        this.ulica=ulica;
        this.brojKuce=brojKuce;
        this.prezime = prezime;
        this.ime = ime;
        this.potvrdaNoveLozinke = potvrdaNoveLozinke;
        this.novaLozinka = novaLozinka;
        this.password = password;
        this.korisnickoIme = korisnickoIme;
    }

    public UpdateProfile() {
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

    public @Size(min = 6, message = "Nova lozinka mora imati min. 6 karaktera.") String getNovaLozinka() {
        return novaLozinka;
    }

    public void setNovaLozinka(@Size(min = 6, message = "Nova lozinka mora imati min. 6 karaktera.") String novaLozinka) {
        this.novaLozinka = novaLozinka;
    }

    public String getPotvrdaNoveLozinke() {
        return potvrdaNoveLozinke;
    }

    public void setPotvrdaNoveLozinke(String potvrdaNoveLozinke) {
        this.potvrdaNoveLozinke = potvrdaNoveLozinke;
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

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public @Pattern(regexp = "^[0-9a-zA-Z/\\- ]{0,15}$", message = "Broj adrese nije validan.") String getBrojKuce() {
        return brojKuce;
    }

    public void setBrojKuce(@Pattern(regexp = "^[0-9a-zA-Z/\\- ]{0,15}$", message = "Broj adrese nije validan.") String brojKuce) {
        this.brojKuce = brojKuce;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

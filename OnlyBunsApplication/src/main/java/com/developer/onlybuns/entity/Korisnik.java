package com.developer.onlybuns.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Table(name = "korisnik")
@Inheritance(strategy=TABLE_PER_CLASS)
public class Korisnik implements UserDetails {

    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;

    @NotBlank(message = "Email je obavezan.")
    @Email(message = "Unesite validnu email adresu.")
    @Column(name="email", unique=true, nullable=false)
    private String email;

    @Column(name="password", unique=false, nullable=false)
    private String password;

    @Column(name="ime", unique=false, nullable=false)
    @NotBlank(message = "Ime je obavezno.")
    private String ime;

    @NotBlank(message = "Prezime je obavezno.")
    @Column(name="prezime", unique=false, nullable=false)
    private String prezime;

    @NotBlank(message = "Korisničko ime je obavezno.")
    @Column(name="korisnickoIme", unique=true, nullable=false)
    private String korisnickoIme;

    @NotBlank(message = "Grad je obavezan.")
    @Pattern(regexp = "^[a-zA-ZčćžšđČĆŽŠĐ\\s]+$", message = "Grad može sadržati samo slova.")
    @Column(name="grad", unique=false, nullable=false)
    private String  grad;

    @NotBlank(message = "Drzava je obavezna.")
    @Column(name="drzava", unique=false, nullable=false)
    private String  drzava;

    @NotBlank(message = "Broj telefona je obavezan.")
    @Pattern(regexp = "\\d{10}", message = "Broj telefona mora sadržati tačno 10 cifara.")
    @Column(name="broj", unique=false, nullable=false)
    private String  broj;

    @Column(name="info", unique=false, nullable=false)
    private String  info;

    @Enumerated(EnumType.STRING)
    @Column(name="uloga", nullable = false)
    private Uloga uloga;

    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;


    public Korisnik() {

    }

    public Korisnik(Integer id, String email, String password, String ime, String prezime, String grad, String drzava, String broj, String info, Uloga uloga) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.grad = grad;
        this.drzava = drzava;
        this.broj = broj;
        this.info = info;
        this.uloga=uloga;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Uloga getUloga() { return uloga; }

    public void setUloga(Uloga uloga) { this.uloga = uloga; }

    public String getKorisnickoIme() { return korisnickoIme; }

    public void setKorisnickoIme(String korisnickoIme) { this.korisnickoIme = korisnickoIme; }

    public Timestamp getLastPasswordResetDate() { return lastPasswordResetDate; }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) { this.lastPasswordResetDate=lastPasswordResetDate; }



}

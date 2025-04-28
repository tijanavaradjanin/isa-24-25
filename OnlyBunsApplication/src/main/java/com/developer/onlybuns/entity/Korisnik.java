package com.developer.onlybuns.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Table(name = "korisnik")
@Inheritance(strategy = InheritanceType.JOINED)
public class Korisnik implements UserDetails {

    @Id
    @SequenceGenerator(name = "korisnikSeqGen", sequenceName = "korisnikSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "korisnikSeqGen")
    public Integer id;

    @Column(name="email", unique=true, nullable=false)
    public String email;

    @Column(name="password", unique=false, nullable=false)
    public String password;

    @Column(name="ime", unique=false, nullable=false)
    public String ime;

    @Column(name="prezime", unique=false, nullable=false)
    public String prezime;

    @Column(name="korisnickoIme", unique=true, nullable=false)
    public String korisnickoIme;

    @Column(name="grad", unique=false, nullable=false)
    public String  grad;

    @Column(name="drzava", unique=false, nullable=false)
    public String  drzava;

    @Column(name="broj", unique=false)
    public String  broj;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uloga_id", nullable = false)
    private Uloga uloga;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "last_password_reset_date")
    public Timestamp lastPasswordResetDate;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    public Korisnik() {

    }

    public Korisnik(Integer id, String email, String password, String ime, String prezime, String grad, String drzava, String broj, Uloga uloga) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.grad = grad;
        this.drzava = drzava;
        this.broj = broj;
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
        return List.of(uloga);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setPassword(String password) {
        Timestamp now = new Timestamp(new Date().getTime());
        this.setLastPasswordResetDate(now);
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

    /*
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    */

    public Uloga getUloga() { return uloga; }

    public void setUloga(Uloga uloga) { this.uloga = uloga; }

    public String getKorisnickoIme() { return korisnickoIme; }

    public void setKorisnickoIme(String korisnickoIme) { this.korisnickoIme = korisnickoIme; }

    public Timestamp getLastPasswordResetDate() { return lastPasswordResetDate; }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) { this.lastPasswordResetDate=lastPasswordResetDate; }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
}

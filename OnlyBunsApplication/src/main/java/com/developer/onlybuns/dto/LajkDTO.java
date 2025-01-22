package com.developer.onlybuns.dto;

import java.time.LocalDateTime;

public class LajkDTO {
    private Integer id;

    private String korisnickoIme;

    private LocalDateTime vremeLajkovanja;

    private Integer idObjave;

    public LajkDTO() {
    }

    public LajkDTO(Integer id, String korisnickoIme, LocalDateTime vremeLajkovanja, Integer idObjave) {
        this.id = id;
        this.korisnickoIme = korisnickoIme;
        this.vremeLajkovanja = vremeLajkovanja;
        this.idObjave = idObjave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public LocalDateTime getVremeLajkovanja() {
        return vremeLajkovanja;
    }

    public void setVremeLajkovanja(LocalDateTime vremeLajkovanja) {
        this.vremeLajkovanja = vremeLajkovanja;
    }

    public Integer getIdObjave() {
        return idObjave;
    }

    public void setIdObjave(Integer idObjave) {
        this.idObjave = idObjave;
    }
}

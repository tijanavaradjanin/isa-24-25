package com.developer.onlybuns.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="komentar", uniqueConstraints = {})  //koji korisnik je komentarisao koju objavu
public class Komentar {
    @Id
    @SequenceGenerator(name = "komentarSeqGen", sequenceName = "komentarSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "komentarSeqGen")
    private Integer id;

    @Column(name="sadrzaj", unique=true, nullable=false)
    private String sadrzaj;

    @Column(name="vremeKomentarisanja", nullable=false)
    private LocalDateTime vremeKomentarisanja;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private RegistrovaniKorisnik registrovaniKorisnik;

    @ManyToOne
    @JoinColumn(name = "objava_id", nullable = false)
    @JsonBackReference
    private Objava objava;

    public Komentar(String sadrzaj, LocalDateTime vremeKomentarisanja, RegistrovaniKorisnik registrovaniKorisnik, Objava objava) {
        this.sadrzaj = sadrzaj;
        this.vremeKomentarisanja = vremeKomentarisanja;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.objava = objava;
    }

    public Komentar() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public LocalDateTime getVremeKomentarisanja() {
        return vremeKomentarisanja;
    }

    public void setVremeKomentarisanja(LocalDateTime vremeKomentarisanja) {
        this.vremeKomentarisanja = vremeKomentarisanja;
    }

    public RegistrovaniKorisnik getRegistrovaniKorisnik() {
        return registrovaniKorisnik;
    }

    public void setRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik) {
        this.registrovaniKorisnik = registrovaniKorisnik;
    }

    public Objava getObjava() {
        return objava;
    }

    public void setObjava(Objava objava) {
        this.objava = objava;
    }
}

package com.developer.onlybuns.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="objava")
public class Objava {
    @Id
    @SequenceGenerator(name = "objavaSeqGen", sequenceName = "objavaSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "objavaSeqGen")
    private Integer id;

    @Column(name="opis", nullable=false)
    private String opis;

    @Column(name="slika", nullable=false)
    private String slika;

    @Column(name = "latituda", nullable = false)
    private Double latituda;                              // geografska sirina-prva koordinata

    @Column(name = "longituda", nullable = false)
    private Double longituda;                             // geografska duzina-druga koordinata

    @Column(name="vremeKreiranja", nullable=false)
    private LocalDateTime vremeKreiranja;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private RegistrovaniKorisnik registrovaniKorisnik;

    @OneToMany(mappedBy = "objava", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    private List<Komentar> komentari;

    @OneToMany(mappedBy = "objava", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    private List<Lajk> lajkovi;

    public Objava(Integer id, String opis, String slika, Double latituda, Double longituda, LocalDateTime vremeKreiranja, RegistrovaniKorisnik registrovaniKorisnik, List<Komentar> komentari, List<Lajk> lajkovi) {
        this.id = id;
        this.opis = opis;
        this.slika = slika;
        this.latituda = latituda;
        this.longituda = longituda;
        this.vremeKreiranja = vremeKreiranja;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.komentari = komentari;
        this.lajkovi = lajkovi;
    }

    public Objava() {
    }

    public Objava(String opis, String slika, Double latituda, Double longituda, LocalDateTime vremeKreiranja, RegistrovaniKorisnik registrovaniKorisnik, List<Komentar> komentari, List<Lajk> lajkovi) {
        this.opis = opis;
        this.slika = slika;
        this.latituda = latituda;
        this.longituda = longituda;
        this.vremeKreiranja = vremeKreiranja;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.komentari = komentari;
        this.lajkovi = lajkovi;
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

    public RegistrovaniKorisnik getRegistrovaniKorisnik() {
        return registrovaniKorisnik;
    }

    public void setRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik) {
        this.registrovaniKorisnik = registrovaniKorisnik;
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
}

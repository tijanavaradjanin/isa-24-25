package com.developer.onlybuns.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lokacija")
public class Lokacija {

    @Id
    @SequenceGenerator(name = "lokacijaSeqGen", sequenceName = "lokacijaSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lokacijaSeqGen")
    private Integer id;

    @Column(name = "latituda", nullable = false)
    private Double latituda;                              // geografska sirina-prva koordinata

    @Column(name = "longituda", nullable = false)
    private Double longituda;                             // geografska duzina-druga koordinata

    @OneToMany(mappedBy = "lokacija", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Objava> objave;

    @OneToMany(mappedBy = "lokacija", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Korisnik> korisnici;

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

    public List<Objava> getObjave() {
        return objave;
    }

    public void setObjave(List<Objava> objave) {
        this.objave = objave;
    }

    public Lokacija(Integer id, Double latituda, Double longituda) {
        this.id = id;
        this.latituda = latituda;
        this.longituda = longituda;
    }

    public Lokacija(Double latituda, Double longituda) {
        this.latituda = latituda;
        this.longituda = longituda;
    }

    public Lokacija() {
    }
}

package com.developer.onlybuns.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="lajk", uniqueConstraints = @UniqueConstraint(columnNames = {"korisnik_id", "objava_id"}))    //koji korisnik je lajkovao koju objavu
public class Lajk {
    @Id
    @SequenceGenerator(name = "lajkSeqGen", sequenceName = "lajkSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lajkSeqGen")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private RegistrovaniKorisnik registrovaniKorisnik;

    @ManyToOne
    @JoinColumn(name = "objava_id", nullable = false)
    @JsonBackReference
    private Objava objava;

    @Column(name="vremeLajkovanja", nullable=false)
    private LocalDateTime vremeLajkovanja;

    public Lajk(RegistrovaniKorisnik registrovaniKorisnik, Objava objava, LocalDateTime vremeLajkovanja) {
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.objava = objava;
        this.vremeLajkovanja = vremeLajkovanja;
    }

    public Lajk() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getVremeLajkovanja() {
        return vremeLajkovanja;
    }

    public void setVremeLajkovanja(LocalDateTime vremeLajkovanja) {
        this.vremeLajkovanja = vremeLajkovanja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lajk lajk = (Lajk) o;
        return id != null && id.equals(lajk.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

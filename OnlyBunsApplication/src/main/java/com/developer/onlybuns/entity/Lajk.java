package com.developer.onlybuns.entity;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(name="lajk", uniqueConstraints = @UniqueConstraint(columnNames = {"korisnik_id", "objava_id"}))    //koji korisnik je lajkovao koju objavu
public class Lajk {
    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private RegistrovaniKorisnik registrovaniKorisnik;

    @ManyToOne
    @JoinColumn(name = "objava_id", nullable = false)
    private Objava objava;

    @Column(name="vremeLajkovanja", nullable=false)
    private LocalDateTime vremeLajkovanja;

    public Lajk(Integer id, RegistrovaniKorisnik registrovaniKorisnik, Objava objava, LocalDateTime vremeLajkovanja) {
        this.id = id;
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
}

package com.developer.onlybuns.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pracenje", uniqueConstraints = @UniqueConstraint(columnNames = {"pratilac_id", "zapraceni_id"}))   //koji korisnik je zapratio kog korisnika
public class Pracenje {
    @Id
    @SequenceGenerator(name = "pracenjeSeqGen", sequenceName = "pracenjeSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pracenjeSeqGen")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pratilac_id", nullable = false)
    private RegistrovaniKorisnik pratilac;

    @ManyToOne
    @JoinColumn(name = "zapraceni_id", nullable = false)
    private RegistrovaniKorisnik zapraceni;

    @Column(name="datum_pracenja", nullable=false)
    private LocalDateTime vremeZapracivanja;


    public Pracenje() {
    }

    public Pracenje(Integer id, RegistrovaniKorisnik pratilac, RegistrovaniKorisnik zapraceni, LocalDateTime vremeZapracivanja) {
        this.id = id;
        this.pratilac = pratilac;
        this.zapraceni = zapraceni;
        this.vremeZapracivanja = vremeZapracivanja;
    }

    public Pracenje(RegistrovaniKorisnik pratilac, RegistrovaniKorisnik zapraceni, LocalDateTime vremeZapracivanja) {
        this.pratilac = pratilac;
        this.zapraceni = zapraceni;
        this.vremeZapracivanja = vremeZapracivanja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RegistrovaniKorisnik getPratilac() {
        return pratilac;
    }

    public void setPratilac(RegistrovaniKorisnik pratilac) {
        this.pratilac = pratilac;
    }

    public RegistrovaniKorisnik getZapraceni() {
        return zapraceni;
    }

    public void setZapraceni(RegistrovaniKorisnik zapraceni) {
        this.zapraceni = zapraceni;
    }

    public LocalDateTime getVremeZapracivanja() {
        return vremeZapracivanja;
    }

    public void setVremeZapracivanja(LocalDateTime vremeZapracivanja) {
        this.vremeZapracivanja = vremeZapracivanja;
    }
}

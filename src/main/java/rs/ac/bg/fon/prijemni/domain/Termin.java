package rs.ac.bg.fon.prijemni.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "termin")
public class Termin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate datum;

    @Column(name = "vreme_pocetka", nullable = false)
    private LocalTime vremePocetka;

    @Enumerated(EnumType.STRING)
    @Column(name = "vrsta_ispita", nullable = false, length = 30)
    private VrstaIspita vrstaIspita;

    @Column(nullable = false, length = 160)
    private String adresa;

    @Column(nullable = false)
    private Integer kapacitet;

    @Column(name = "popunjeno_mesta", nullable = false)
    private Integer popunjenoMesta = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cena;

    public Termin() {
    }

    public Termin(LocalDate datum, LocalTime vremePocetka, VrstaIspita vrstaIspita,
                  String adresa, Integer kapacitet, BigDecimal cena) {
        this.datum = datum;
        this.vremePocetka = vremePocetka;
        this.vrstaIspita = vrstaIspita;
        this.adresa = adresa;
        this.kapacitet = kapacitet;
        this.cena = cena;
    }

    @Transient
    public int getSlobodnihMesta() {
        return kapacitet - popunjenoMesta;
    }

    public void zauzmiMesto() {
        popunjenoMesta = popunjenoMesta + 1;
    }

    public void oslobodiMesto() {
        if (popunjenoMesta > 0) {
            popunjenoMesta = popunjenoMesta - 1;
        }
    }

    public Long getId() { return id; }
    public LocalDate getDatum() { return datum; }
    public LocalTime getVremePocetka() { return vremePocetka; }
    public VrstaIspita getVrstaIspita() { return vrstaIspita; }
    public String getAdresa() { return adresa; }
    public Integer getKapacitet() { return kapacitet; }
    public Integer getPopunjenoMesta() { return popunjenoMesta; }
    public BigDecimal getCena() { return cena; }

    public void setDatum(LocalDate datum) { this.datum = datum; }
    public void setVremePocetka(LocalTime vremePocetka) { this.vremePocetka = vremePocetka; }
    public void setVrstaIspita(VrstaIspita vrstaIspita) { this.vrstaIspita = vrstaIspita; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    public void setKapacitet(Integer kapacitet) { this.kapacitet = kapacitet; }
    public void setCena(BigDecimal cena) { this.cena = cena; }
}

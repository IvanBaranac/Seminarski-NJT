package rs.ac.bg.fon.prijemni.dto;

import rs.ac.bg.fon.prijemni.domain.VrstaIspita;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class TerminDto {

    private Long id;
    private LocalDate datum;
    private LocalTime vremePocetka;
    private VrstaIspita vrstaIspita;
    private String nazivVrste;
    private String adresa;
    private Integer kapacitet;
    private Integer popunjenoMesta;
    private Integer slobodnihMesta;
    private BigDecimal cena;

    public TerminDto() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }

    public LocalTime getVremePocetka() { return vremePocetka; }
    public void setVremePocetka(LocalTime vremePocetka) { this.vremePocetka = vremePocetka; }

    public VrstaIspita getVrstaIspita() { return vrstaIspita; }
    public void setVrstaIspita(VrstaIspita vrstaIspita) { this.vrstaIspita = vrstaIspita; }

    public String getNazivVrste() { return nazivVrste; }
    public void setNazivVrste(String nazivVrste) { this.nazivVrste = nazivVrste; }

    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    public Integer getKapacitet() { return kapacitet; }
    public void setKapacitet(Integer kapacitet) { this.kapacitet = kapacitet; }

    public Integer getPopunjenoMesta() { return popunjenoMesta; }
    public void setPopunjenoMesta(Integer popunjenoMesta) { this.popunjenoMesta = popunjenoMesta; }

    public Integer getSlobodnihMesta() { return slobodnihMesta; }
    public void setSlobodnihMesta(Integer slobodnihMesta) { this.slobodnihMesta = slobodnihMesta; }

    public BigDecimal getCena() { return cena; }
    public void setCena(BigDecimal cena) { this.cena = cena; }
}

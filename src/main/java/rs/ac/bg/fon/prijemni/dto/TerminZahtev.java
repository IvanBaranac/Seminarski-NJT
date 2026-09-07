package rs.ac.bg.fon.prijemni.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import rs.ac.bg.fon.prijemni.domain.VrstaIspita;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class TerminZahtev {

    @NotNull(message = "Datum je obavezan")
    private LocalDate datum;

    @NotNull(message = "Vreme je obavezno")
    private LocalTime vremePocetka;

    @NotNull(message = "Vrsta ispita je obavezna")
    private VrstaIspita vrstaIspita;

    @NotBlank(message = "Adresa je obavezna")
    private String adresa;

    @NotNull(message = "Kapacitet je obavezan")
    private Integer kapacitet;

    @NotNull(message = "Cena je obavezna")
    private BigDecimal cena;

    public LocalDate getDatum() { return datum; }
    public void setDatum(LocalDate datum) { this.datum = datum; }

    public LocalTime getVremePocetka() { return vremePocetka; }
    public void setVremePocetka(LocalTime vremePocetka) { this.vremePocetka = vremePocetka; }

    public VrstaIspita getVrstaIspita() { return vrstaIspita; }
    public void setVrstaIspita(VrstaIspita vrstaIspita) { this.vrstaIspita = vrstaIspita; }

    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    public Integer getKapacitet() { return kapacitet; }
    public void setKapacitet(Integer kapacitet) { this.kapacitet = kapacitet; }

    public BigDecimal getCena() { return cena; }
    public void setCena(BigDecimal cena) { this.cena = cena; }
}

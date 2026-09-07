package rs.ac.bg.fon.prijemni.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrijavaDto {

    private Long id;
    private String kandidat;
    private String email;
    private String telefon;
    private String skola;
    private LocalDate datumPrijave;
    private String status;
    private String nazivStatusa;
    private BigDecimal osnovnaCena;
    private Integer popustProcenat;
    private BigDecimal ukupnaCena;
    private String pozivNaBroj;
    private LocalDate rokZaUplatu;
    private String nacinPlacanja;
    private List<StavkaDto> stavke = new ArrayList<>();

    public PrijavaDto() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKandidat() { return kandidat; }
    public void setKandidat(String kandidat) { this.kandidat = kandidat; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getSkola() { return skola; }
    public void setSkola(String skola) { this.skola = skola; }

    public LocalDate getDatumPrijave() { return datumPrijave; }
    public void setDatumPrijave(LocalDate datumPrijave) { this.datumPrijave = datumPrijave; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNazivStatusa() { return nazivStatusa; }
    public void setNazivStatusa(String nazivStatusa) { this.nazivStatusa = nazivStatusa; }

    public BigDecimal getOsnovnaCena() { return osnovnaCena; }
    public void setOsnovnaCena(BigDecimal osnovnaCena) { this.osnovnaCena = osnovnaCena; }

    public Integer getPopustProcenat() { return popustProcenat; }
    public void setPopustProcenat(Integer popustProcenat) { this.popustProcenat = popustProcenat; }

    public BigDecimal getUkupnaCena() { return ukupnaCena; }
    public void setUkupnaCena(BigDecimal ukupnaCena) { this.ukupnaCena = ukupnaCena; }

    public String getPozivNaBroj() { return pozivNaBroj; }
    public void setPozivNaBroj(String pozivNaBroj) { this.pozivNaBroj = pozivNaBroj; }

    public LocalDate getRokZaUplatu() { return rokZaUplatu; }
    public void setRokZaUplatu(LocalDate rokZaUplatu) { this.rokZaUplatu = rokZaUplatu; }

    public String getNacinPlacanja() { return nacinPlacanja; }
    public void setNacinPlacanja(String nacinPlacanja) { this.nacinPlacanja = nacinPlacanja; }

    public List<StavkaDto> getStavke() { return stavke; }
    public void setStavke(List<StavkaDto> stavke) { this.stavke = stavke; }
}

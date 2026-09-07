package rs.ac.bg.fon.prijemni.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prijava")
public class Prijava {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "kandidat_id")
    private Korisnik kandidat;

    @OneToMany(mappedBy = "prijava", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StavkaPrijave> stavke = new ArrayList<>();

    @Column(name = "datum_prijave", nullable = false)
    private LocalDate datumPrijave = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPrijave status = StatusPrijave.CEKA_UPLATU;

    @Column(name = "osnovna_cena", nullable = false, precision = 10, scale = 2)
    private BigDecimal osnovnaCena;

    @Column(name = "popust_procenat", nullable = false)
    private Integer popustProcenat;

    @Column(name = "ukupna_cena", nullable = false, precision = 10, scale = 2)
    private BigDecimal ukupnaCena;

    @Column(name = "poziv_na_broj", nullable = false, unique = true, length = 30)
    private String pozivNaBroj;

    @Column(name = "rok_za_uplatu", nullable = false)
    private LocalDate rokZaUplatu;

    @Column(name = "nacin_placanja", length = 20)
    private String nacinPlacanja;

    @Column(name = "datum_uplate")
    private LocalDate datumUplate;

    public Prijava() {
    }

    public Prijava(Korisnik kandidat) {
        this.kandidat = kandidat;
    }

    public void dodajStavku(StavkaPrijave stavka) {
        stavka.setPrijava(this);
        stavke.add(stavka);
    }

    public void oznaciKaoPlacenu(String nacinPlacanja) {
        this.nacinPlacanja = nacinPlacanja;
        this.datumUplate = LocalDate.now();
        this.status = StatusPrijave.PRIJAVLJEN;
    }

    public void otkazi() {
        for (StavkaPrijave stavka : stavke) {
            stavka.getTermin().oslobodiMesto();
        }
        this.status = StatusPrijave.OTKAZANA;
    }

    public Long getId() { return id; }
    public Korisnik getKandidat() { return kandidat; }
    public List<StavkaPrijave> getStavke() { return stavke; }
    public LocalDate getDatumPrijave() { return datumPrijave; }
    public StatusPrijave getStatus() { return status; }
    public BigDecimal getOsnovnaCena() { return osnovnaCena; }
    public Integer getPopustProcenat() { return popustProcenat; }
    public BigDecimal getUkupnaCena() { return ukupnaCena; }
    public String getPozivNaBroj() { return pozivNaBroj; }
    public LocalDate getRokZaUplatu() { return rokZaUplatu; }
    public String getNacinPlacanja() { return nacinPlacanja; }
    public LocalDate getDatumUplate() { return datumUplate; }

    public void setOsnovnaCena(BigDecimal osnovnaCena) { this.osnovnaCena = osnovnaCena; }
    public void setPopustProcenat(Integer popustProcenat) { this.popustProcenat = popustProcenat; }
    public void setUkupnaCena(BigDecimal ukupnaCena) { this.ukupnaCena = ukupnaCena; }
    public void setPozivNaBroj(String pozivNaBroj) { this.pozivNaBroj = pozivNaBroj; }
    public void setRokZaUplatu(LocalDate rokZaUplatu) { this.rokZaUplatu = rokZaUplatu; }
}

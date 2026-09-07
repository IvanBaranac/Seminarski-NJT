package rs.ac.bg.fon.prijemni.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

@Entity
@Table(name = "stavka_prijave",
       uniqueConstraints = @UniqueConstraint(columnNames = {"prijava_id", "termin_id"}))
public class StavkaPrijave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "prijava_id")
    private Prijava prijava;

    @ManyToOne(optional = false)
    @JoinColumn(name = "termin_id")
    private Termin termin;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cena;

    public StavkaPrijave() {
    }

    public StavkaPrijave(Termin termin, BigDecimal cena) {
        this.termin = termin;
        this.cena = cena;
    }

    public Long getId() { return id; }
    public Prijava getPrijava() { return prijava; }
    public Termin getTermin() { return termin; }
    public BigDecimal getCena() { return cena; }

    public void setPrijava(Prijava prijava) { this.prijava = prijava; }
}

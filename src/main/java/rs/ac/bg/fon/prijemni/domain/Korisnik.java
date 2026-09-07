package rs.ac.bg.fon.prijemni.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "korisnik")
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String ime;

    @Column(nullable = false, length = 60)
    private String prezime;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 100)
    private String lozinka;

    @Column(length = 30)
    private String telefon;

    @Column(name = "srednja_skola", length = 120)
    private String srednjaSkola;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Uloga uloga;

    public Korisnik() {
    }

    public Korisnik(String ime, String prezime, String email, String lozinka, Uloga uloga) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
        this.uloga = uloga;
    }

    public String getPunoIme() {
        return ime + " " + prezime;
    }

    public boolean jeAdmin() {
        return uloga == Uloga.ADMIN;
    }

    public Long getId() { return id; }
    public String getIme() { return ime; }
    public String getPrezime() { return prezime; }
    public String getEmail() { return email; }
    public String getLozinka() { return lozinka; }
    public String getTelefon() { return telefon; }
    public String getSrednjaSkola() { return srednjaSkola; }
    public Uloga getUloga() { return uloga; }

    public void setTelefon(String telefon) { this.telefon = telefon; }
    public void setSrednjaSkola(String srednjaSkola) { this.srednjaSkola = srednjaSkola; }
}

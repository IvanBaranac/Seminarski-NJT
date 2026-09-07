package rs.ac.bg.fon.prijemni.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistracijaZahtev {

    @NotBlank(message = "Ime je obavezno")
    private String ime;

    @NotBlank(message = "Prezime je obavezno")
    private String prezime;

    @NotBlank
    @Email(message = "Neispravna mejl adresa")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Lozinka mora imati najmanje 6 karaktera")
    private String lozinka;

    private String telefon;

    private String srednjaSkola;

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLozinka() { return lozinka; }
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getSrednjaSkola() { return srednjaSkola; }
    public void setSrednjaSkola(String srednjaSkola) { this.srednjaSkola = srednjaSkola; }
}

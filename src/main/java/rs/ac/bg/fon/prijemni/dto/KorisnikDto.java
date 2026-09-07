package rs.ac.bg.fon.prijemni.dto;

public class KorisnikDto {

    private Long id;
    private String ime;
    private String prezime;
    private String email;
    private String uloga;

    public KorisnikDto() {
    }

    public KorisnikDto(Long id, String ime, String prezime, String email, String uloga) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.uloga = uloga;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUloga() { return uloga; }
    public void setUloga(String uloga) { this.uloga = uloga; }
}

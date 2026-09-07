package rs.ac.bg.fon.prijemni.dto;

public class LoginOdgovor {

    private String token;
    private KorisnikDto korisnik;

    public LoginOdgovor() {
    }

    public LoginOdgovor(String token, KorisnikDto korisnik) {
        this.token = token;
        this.korisnik = korisnik;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public KorisnikDto getKorisnik() { return korisnik; }
    public void setKorisnik(KorisnikDto korisnik) { this.korisnik = korisnik; }
}

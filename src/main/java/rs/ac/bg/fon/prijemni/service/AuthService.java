package rs.ac.bg.fon.prijemni.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.prijemni.domain.Korisnik;
import rs.ac.bg.fon.prijemni.domain.Uloga;
import rs.ac.bg.fon.prijemni.dto.KorisnikDto;
import rs.ac.bg.fon.prijemni.dto.LoginOdgovor;
import rs.ac.bg.fon.prijemni.dto.LoginZahtev;
import rs.ac.bg.fon.prijemni.dto.RegistracijaZahtev;
import rs.ac.bg.fon.prijemni.exception.PoslovnaGreska;
import rs.ac.bg.fon.prijemni.repository.KorisnikRepository;
import rs.ac.bg.fon.prijemni.security.JwtUtil;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder sifrator;
    private final JwtUtil jwtUtil;

    public AuthService(KorisnikRepository korisnikRepository, PasswordEncoder sifrator, JwtUtil jwtUtil) {
        this.korisnikRepository = korisnikRepository;
        this.sifrator = sifrator;
        this.jwtUtil = jwtUtil;
    }

    public LoginOdgovor registruj(RegistracijaZahtev zahtev) {
        String email = zahtev.getEmail().trim().toLowerCase();

        if (korisnikRepository.existsByEmail(email)) {
            throw new PoslovnaGreska("Nalog sa ovom mejl adresom već postoji.");
        }

        String sifrovanaLozinka = sifrator.encode(zahtev.getLozinka());

        Korisnik korisnik = new Korisnik(zahtev.getIme().trim(), zahtev.getPrezime().trim(),
                email, sifrovanaLozinka, Uloga.KANDIDAT);

        korisnik.setTelefon(zahtev.getTelefon());
        korisnik.setSrednjaSkola(zahtev.getSrednjaSkola());

        korisnikRepository.save(korisnik);

        return napraviOdgovor(korisnik);
    }

    public LoginOdgovor prijavi(LoginZahtev zahtev) {
        String email = zahtev.getEmail().trim().toLowerCase();
        Optional<Korisnik> pronadjen = korisnikRepository.findByEmail(email);

        if (pronadjen.isEmpty()) {
            throw new PoslovnaGreska("Pogrešna mejl adresa ili lozinka.");
        }

        Korisnik korisnik = pronadjen.get();

        if (!sifrator.matches(zahtev.getLozinka(), korisnik.getLozinka())) {
            throw new PoslovnaGreska("Pogrešna mejl adresa ili lozinka.");
        }

        return napraviOdgovor(korisnik);
    }

    @Transactional(readOnly = true)
    public Korisnik nadjiPoEmailu(String email) {
        Optional<Korisnik> pronadjen = korisnikRepository.findByEmail(email);

        if (pronadjen.isEmpty()) {
            throw new PoslovnaGreska("Korisnik nije pronađen.");
        }

        return pronadjen.get();
    }

    private LoginOdgovor napraviOdgovor(Korisnik korisnik) {
        String token = jwtUtil.napraviToken(korisnik.getEmail(), korisnik.getUloga().name());

        return new LoginOdgovor(token, uDto(korisnik));
    }

    public static KorisnikDto uDto(Korisnik korisnik) {
        return new KorisnikDto(korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(),
                korisnik.getEmail(), korisnik.getUloga().name());
    }
}

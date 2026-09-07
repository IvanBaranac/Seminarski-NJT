package rs.ac.bg.fon.prijemni.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.prijemni.domain.Korisnik;
import rs.ac.bg.fon.prijemni.domain.Termin;
import rs.ac.bg.fon.prijemni.domain.Uloga;
import rs.ac.bg.fon.prijemni.domain.VrstaIspita;
import rs.ac.bg.fon.prijemni.repository.KorisnikRepository;
import rs.ac.bg.fon.prijemni.repository.TerminRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DemoPodaci implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoPodaci.class);

    private static final String FON = "FON, Jove Ilića 154, Beograd";
    private static final String NOVI_SAD = "Gimnazija Jovan Jovanović Zmaj, Zlatne grede 4, Novi Sad";

    private final KorisnikRepository korisnikRepository;
    private final TerminRepository terminRepository;
    private final PasswordEncoder sifrator;

    public DemoPodaci(KorisnikRepository korisnikRepository,
                      TerminRepository terminRepository,
                      PasswordEncoder sifrator) {
        this.korisnikRepository = korisnikRepository;
        this.terminRepository = terminRepository;
        this.sifrator = sifrator;
    }

    @Override
    public void run(String... args) {
        if (korisnikRepository.count() > 0) {
            return;
        }

        korisnikRepository.save(new Korisnik("Marija", "Jovanović", "admin@fon.bg.ac.rs",
                sifrator.encode("admin123"), Uloga.ADMIN));

        Korisnik pera = new Korisnik("Petar", "Petrović", "pera@primer.rs",
                sifrator.encode("kandidat123"), Uloga.KANDIDAT);
        pera.setTelefon("064/123-456");
        pera.setSrednjaSkola("Gimnazija Sveti Sava, Beograd");
        korisnikRepository.save(pera);

        Korisnik jovana = new Korisnik("Jovana", "Ilić", "jovana@primer.rs",
                sifrator.encode("kandidat123"), Uloga.KANDIDAT);
        jovana.setTelefon("063/987-654");
        jovana.setSrednjaSkola("Matematička gimnazija, Beograd");
        korisnikRepository.save(jovana);

        LocalDate danas = LocalDate.now();

        terminRepository.save(new Termin(danas.plusDays(7), LocalTime.of(10, 0),
                VrstaIspita.MATEMATIKA, FON, 40, new BigDecimal("2500.00")));

        terminRepository.save(new Termin(danas.plusDays(10), LocalTime.of(13, 0),
                VrstaIspita.OPSTA_INFORMISANOST, FON, 40, new BigDecimal("2000.00")));

        terminRepository.save(new Termin(danas.plusDays(14), LocalTime.of(10, 0),
                VrstaIspita.MATEMATIKA, NOVI_SAD, 30, new BigDecimal("2500.00")));

        terminRepository.save(new Termin(danas.plusDays(21), LocalTime.of(11, 0),
                VrstaIspita.OPSTA_INFORMISANOST, NOVI_SAD, 30, new BigDecimal("2000.00")));

        terminRepository.save(new Termin(danas.plusDays(28), LocalTime.of(10, 0),
                VrstaIspita.MATEMATIKA, FON, 50, new BigDecimal("2500.00")));

        log.info("Baza je bila prazna, ucitani su pocetni podaci.");
    }
}

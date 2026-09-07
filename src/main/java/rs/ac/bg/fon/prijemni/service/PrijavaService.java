package rs.ac.bg.fon.prijemni.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.prijemni.domain.Korisnik;
import rs.ac.bg.fon.prijemni.domain.Prijava;
import rs.ac.bg.fon.prijemni.domain.StatusPrijave;
import rs.ac.bg.fon.prijemni.domain.StavkaPrijave;
import rs.ac.bg.fon.prijemni.domain.Termin;
import rs.ac.bg.fon.prijemni.dto.NovaPrijavaZahtev;
import rs.ac.bg.fon.prijemni.dto.PrijavaDto;
import rs.ac.bg.fon.prijemni.dto.StavkaDto;
import rs.ac.bg.fon.prijemni.exception.PoslovnaGreska;
import rs.ac.bg.fon.prijemni.repository.PrijavaRepository;
import rs.ac.bg.fon.prijemni.repository.TerminRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PrijavaService {

    private static final DateTimeFormatter DATUM = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private static final int MAX_TERMINA = 5;

    private final PrijavaRepository prijavaRepository;
    private final TerminRepository terminRepository;
    private final EmailService emailService;
    private final DokumentService dokumentService;
    private final int rokUplateDana;

    public PrijavaService(PrijavaRepository prijavaRepository,
                          TerminRepository terminRepository,
                          EmailService emailService,
                          DokumentService dokumentService,
                          @Value("${app.rok-uplate-dana}") int rokUplateDana) {
        this.prijavaRepository = prijavaRepository;
        this.terminRepository = terminRepository;
        this.emailService = emailService;
        this.dokumentService = dokumentService;
        this.rokUplateDana = rokUplateDana;
    }

    public static int popustZa(int brojTermina) {
        if (brojTermina >= 3) {
            return 15;
        }

        if (brojTermina == 2) {
            return 10;
        }

        return 0;
    }

    public static String pozivNaBroj(Long prijavaId) {
        String osnovica = LocalDate.now().getYear() + String.format("%05d", prijavaId);
        long ostatak = Long.parseLong(osnovica + "00") % 97;
        long kontrolniBroj = 98 - ostatak;

        return String.format("%02d", kontrolniBroj) + "-" + osnovica;
    }

    public PrijavaDto kreiraj(Korisnik kandidat, NovaPrijavaZahtev zahtev) {
        List<Long> terminIds = new ArrayList<>();

        for (Long terminId : zahtev.getTerminIds()) {
            if (!terminIds.contains(terminId)) {
                terminIds.add(terminId);
            }
        }

        if (terminIds.size() > MAX_TERMINA) {
            throw new PoslovnaGreska("U jednu prijavu možete dodati najviše " + MAX_TERMINA + " termina.");
        }

        List<LocalDate> zauzetiDatumi =
                new ArrayList<>(prijavaRepository.nadjiZauzeteDatume(kandidat.getId()));

        Prijava prijava = new Prijava(kandidat);
        BigDecimal osnovnaCena = BigDecimal.ZERO;

        for (Long terminId : terminIds) {
            Termin termin = nadjiTermin(terminId);

            if (termin.getDatum().isBefore(LocalDate.now())) {
                throw new PoslovnaGreska("Termin je već prošao.");
            }

            if (termin.getSlobodnihMesta() <= 0) {
                throw new PoslovnaGreska("Termin " + DokumentService.opisTermina(termin) + " je popunjen.");
            }

            if (zauzetiDatumi.contains(termin.getDatum())) {
                throw new PoslovnaGreska("Već imate prijavu za " + termin.getDatum().format(DATUM)
                        + ". Na jedan dan možete izaći samo na jedan termin.");
            }

            zauzetiDatumi.add(termin.getDatum());
            termin.zauzmiMesto();

            StavkaPrijave stavka = new StavkaPrijave(termin, termin.getCena());
            prijava.dodajStavku(stavka);

            osnovnaCena = osnovnaCena.add(termin.getCena());
        }

        int popust = popustZa(terminIds.size());

        BigDecimal umanjenje = osnovnaCena.multiply(BigDecimal.valueOf(popust))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        prijava.setOsnovnaCena(osnovnaCena);
        prijava.setPopustProcenat(popust);
        prijava.setUkupnaCena(osnovnaCena.subtract(umanjenje));
        prijava.setRokZaUplatu(LocalDate.now().plusDays(rokUplateDana));
        prijava.setPozivNaBroj("privremeno");

        prijavaRepository.saveAndFlush(prijava);

        prijava.setPozivNaBroj(pozivNaBroj(prijava.getId()));

        emailService.posaljiUplatnicu(prijava);

        return uDto(prijava);
    }

    @Transactional(readOnly = true)
    public List<PrijavaDto> mojePrijave(Long kandidatId) {
        return uListuDto(prijavaRepository.findByKandidatIdOrderByIdDesc(kandidatId));
    }

    @Transactional(readOnly = true)
    public List<PrijavaDto> svePrijave() {
        return uListuDto(prijavaRepository.findAllByOrderByIdDesc());
    }

    @Transactional(readOnly = true)
    public List<PrijavaDto> prijavljeniZaTermin(Long terminId) {
        return uListuDto(prijavaRepository.nadjiPrijavljeneZaTermin(terminId));
    }

    @Transactional(readOnly = true)
    public byte[] uplatnicaPdf(Long prijavaId, Korisnik korisnik) {
        Prijava prijava = nadji(prijavaId);
        proveriVlasnika(prijava, korisnik);

        return dokumentService.uplatnicaPdf(prijava);
    }

    @Transactional(readOnly = true)
    public byte[] spisakExcel(Termin termin) {
        List<Prijava> prijave = prijavaRepository.nadjiPrijavljeneZaTermin(termin.getId());

        return dokumentService.spisakExcel(termin, prijave);
    }

    public PrijavaDto otkazi(Long prijavaId, Korisnik korisnik) {
        Prijava prijava = nadji(prijavaId);
        proveriVlasnika(prijava, korisnik);

        if (prijava.getStatus() != StatusPrijave.CEKA_UPLATU) {
            throw new PoslovnaGreska("Prijava se može otkazati samo dok čeka uplatu.");
        }

        prijava.otkazi();

        return uDto(prijava);
    }

    public PrijavaDto evidentirajUplatu(Long prijavaId, String nacinPlacanja) {
        Prijava prijava = nadji(prijavaId);

        if (prijava.getStatus() != StatusPrijave.CEKA_UPLATU) {
            throw new PoslovnaGreska("Uplata se može evidentirati samo za prijavu koja čeka uplatu.");
        }

        prijava.oznaciKaoPlacenu(nacinPlacanja);

        emailService.posaljiPotvrduUplate(prijava);

        return uDto(prijava);
    }

    private Prijava nadji(Long id) {
        Optional<Prijava> pronadjena = prijavaRepository.findById(id);

        if (pronadjena.isEmpty()) {
            throw new PoslovnaGreska("Prijava nije pronađena.");
        }

        return pronadjena.get();
    }

    private Termin nadjiTermin(Long id) {
        Optional<Termin> pronadjen = terminRepository.findById(id);

        if (pronadjen.isEmpty()) {
            throw new PoslovnaGreska("Termin nije pronađen.");
        }

        return pronadjen.get();
    }

    private void proveriVlasnika(Prijava prijava, Korisnik korisnik) {
        if (korisnik.jeAdmin()) {
            return;
        }

        if (!prijava.getKandidat().getId().equals(korisnik.getId())) {
            throw new PoslovnaGreska("Ova prijava ne pripada vama.");
        }
    }

    private List<PrijavaDto> uListuDto(List<Prijava> prijave) {
        List<PrijavaDto> lista = new ArrayList<>();

        for (Prijava prijava : prijave) {
            lista.add(uDto(prijava));
        }

        return lista;
    }

    public static PrijavaDto uDto(Prijava prijava) {
        List<StavkaDto> stavke = new ArrayList<>();

        for (StavkaPrijave stavka : prijava.getStavke()) {
            String opis = DokumentService.opisTermina(stavka.getTermin());
            stavke.add(new StavkaDto(stavka.getTermin().getId(), opis, stavka.getCena()));
        }

        Korisnik kandidat = prijava.getKandidat();
        PrijavaDto dto = new PrijavaDto();

        dto.setId(prijava.getId());
        dto.setKandidat(kandidat.getPunoIme());
        dto.setEmail(kandidat.getEmail());
        dto.setTelefon(kandidat.getTelefon());
        dto.setSkola(kandidat.getSrednjaSkola());
        dto.setDatumPrijave(prijava.getDatumPrijave());
        dto.setStatus(prijava.getStatus().name());
        dto.setNazivStatusa(prijava.getStatus().getNaziv());
        dto.setOsnovnaCena(prijava.getOsnovnaCena());
        dto.setPopustProcenat(prijava.getPopustProcenat());
        dto.setUkupnaCena(prijava.getUkupnaCena());
        dto.setPozivNaBroj(prijava.getPozivNaBroj());
        dto.setRokZaUplatu(prijava.getRokZaUplatu());
        dto.setNacinPlacanja(prijava.getNacinPlacanja());
        dto.setStavke(stavke);

        return dto;
    }
}

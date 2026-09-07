package rs.ac.bg.fon.prijemni.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.prijemni.domain.Termin;
import rs.ac.bg.fon.prijemni.dto.TerminDto;
import rs.ac.bg.fon.prijemni.dto.TerminZahtev;
import rs.ac.bg.fon.prijemni.exception.PoslovnaGreska;
import rs.ac.bg.fon.prijemni.repository.TerminRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TerminService {

    private final TerminRepository terminRepository;

    public TerminService(TerminRepository terminRepository) {
        this.terminRepository = terminRepository;
    }

    @Transactional(readOnly = true)
    public List<TerminDto> predstojeci() {
        List<Termin> termini = terminRepository
                .findByDatumGreaterThanEqualOrderByDatumAscVremePocetkaAsc(LocalDate.now());

        return uListuDto(termini);
    }

    @Transactional(readOnly = true)
    public List<TerminDto> svi() {
        List<Termin> termini = terminRepository.findAllByOrderByDatumDescVremePocetkaDesc();

        return uListuDto(termini);
    }

    public TerminDto kreiraj(TerminZahtev zahtev) {
        if (zahtev.getDatum().isBefore(LocalDate.now())) {
            throw new PoslovnaGreska("Termin se ne može zakazati u prošlosti.");
        }

        Termin termin = new Termin(zahtev.getDatum(), zahtev.getVremePocetka(), zahtev.getVrstaIspita(),
                zahtev.getAdresa(), zahtev.getKapacitet(), zahtev.getCena());

        terminRepository.save(termin);

        return uDto(termin);
    }

    public TerminDto izmeni(Long id, TerminZahtev zahtev) {
        Termin termin = nadji(id);

        if (zahtev.getKapacitet() < termin.getPopunjenoMesta()) {
            throw new PoslovnaGreska("Kapacitet ne može biti manji od broja prijavljenih ("
                    + termin.getPopunjenoMesta() + ").");
        }

        termin.setDatum(zahtev.getDatum());
        termin.setVremePocetka(zahtev.getVremePocetka());
        termin.setVrstaIspita(zahtev.getVrstaIspita());
        termin.setAdresa(zahtev.getAdresa());
        termin.setKapacitet(zahtev.getKapacitet());
        termin.setCena(zahtev.getCena());

        return uDto(termin);
    }

    public void obrisi(Long id) {
        Termin termin = nadji(id);

        if (termin.getPopunjenoMesta() > 0) {
            throw new PoslovnaGreska("Termin ima prijavljene kandidate i ne može se obrisati.");
        }

        terminRepository.delete(termin);
    }

    @Transactional(readOnly = true)
    public Termin nadji(Long id) {
        Optional<Termin> pronadjen = terminRepository.findById(id);

        if (pronadjen.isEmpty()) {
            throw new PoslovnaGreska("Termin nije pronađen.");
        }

        return pronadjen.get();
    }

    private List<TerminDto> uListuDto(List<Termin> termini) {
        List<TerminDto> lista = new ArrayList<>();

        for (Termin termin : termini) {
            lista.add(uDto(termin));
        }

        return lista;
    }

    public static TerminDto uDto(Termin termin) {
        TerminDto dto = new TerminDto();

        dto.setId(termin.getId());
        dto.setDatum(termin.getDatum());
        dto.setVremePocetka(termin.getVremePocetka());
        dto.setVrstaIspita(termin.getVrstaIspita());
        dto.setNazivVrste(termin.getVrstaIspita().getNaziv());
        dto.setAdresa(termin.getAdresa());
        dto.setKapacitet(termin.getKapacitet());
        dto.setPopunjenoMesta(termin.getPopunjenoMesta());
        dto.setSlobodnihMesta(termin.getSlobodnihMesta());
        dto.setCena(termin.getCena());

        return dto;
    }
}

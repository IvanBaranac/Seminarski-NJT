package rs.ac.bg.fon.prijemni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.prijemni.domain.Termin;

import java.time.LocalDate;
import java.util.List;

public interface TerminRepository extends JpaRepository<Termin, Long> {

    List<Termin> findByDatumGreaterThanEqualOrderByDatumAscVremePocetkaAsc(LocalDate datum);

    List<Termin> findAllByOrderByDatumDescVremePocetkaDesc();
}

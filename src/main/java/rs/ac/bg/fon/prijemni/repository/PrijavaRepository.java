package rs.ac.bg.fon.prijemni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.bg.fon.prijemni.domain.Prijava;

import java.time.LocalDate;
import java.util.List;

public interface PrijavaRepository extends JpaRepository<Prijava, Long> {

    List<Prijava> findByKandidatIdOrderByIdDesc(Long kandidatId);

    List<Prijava> findAllByOrderByIdDesc();

    @Query("select s.termin.datum from Prijava p join p.stavke s "
         + "where p.kandidat.id = :kandidatId "
         + "and p.status <> rs.ac.bg.fon.prijemni.domain.StatusPrijave.OTKAZANA")
    List<LocalDate> nadjiZauzeteDatume(@Param("kandidatId") Long kandidatId);

    @Query("select distinct p from Prijava p join p.stavke s "
         + "where s.termin.id = :terminId and p.status = rs.ac.bg.fon.prijemni.domain.StatusPrijave.PRIJAVLJEN "
         + "order by p.kandidat.prezime, p.kandidat.ime")
    List<Prijava> nadjiPrijavljeneZaTermin(@Param("terminId") Long terminId);
}

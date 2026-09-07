package rs.ac.bg.fon.prijemni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.prijemni.domain.Korisnik;

import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    Optional<Korisnik> findByEmail(String email);

    boolean existsByEmail(String email);
}

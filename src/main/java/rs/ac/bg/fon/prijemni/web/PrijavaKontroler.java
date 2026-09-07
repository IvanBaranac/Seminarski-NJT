package rs.ac.bg.fon.prijemni.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.prijemni.domain.Korisnik;
import rs.ac.bg.fon.prijemni.dto.NovaPrijavaZahtev;
import rs.ac.bg.fon.prijemni.dto.PrijavaDto;
import rs.ac.bg.fon.prijemni.dto.UplataZahtev;
import rs.ac.bg.fon.prijemni.service.AuthService;
import rs.ac.bg.fon.prijemni.service.PrijavaService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PrijavaKontroler {

    private final PrijavaService prijavaService;
    private final AuthService authService;

    public PrijavaKontroler(PrijavaService prijavaService, AuthService authService) {
        this.prijavaService = prijavaService;
        this.authService = authService;
    }

    @PostMapping("/prijave")
    public ResponseEntity<PrijavaDto> kreiraj(@AuthenticationPrincipal String email,
                                                   @Valid @RequestBody NovaPrijavaZahtev zahtev) {
        Korisnik kandidat = authService.nadjiPoEmailu(email);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prijavaService.kreiraj(kandidat, zahtev));
    }

    @GetMapping("/prijave/moje")
    public List<PrijavaDto> moje(@AuthenticationPrincipal String email) {
        return prijavaService.mojePrijave(authService.nadjiPoEmailu(email).getId());
    }

    @GetMapping("/prijave/{id}/uplatnica")
    public ResponseEntity<byte[]> uplatnica(@AuthenticationPrincipal String email, @PathVariable Long id) {
        Korisnik korisnik = authService.nadjiPoEmailu(email);
        byte[] pdf = prijavaService.uplatnicaPdf(id, korisnik);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=uplatnica-" + id + ".pdf")
                .body(pdf);
    }

    @DeleteMapping("/prijave/{id}")
    public PrijavaDto otkazi(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return prijavaService.otkazi(id, authService.nadjiPoEmailu(email));
    }

    @GetMapping("/admin/prijave")
    public List<PrijavaDto> sve() {
        return prijavaService.svePrijave();
    }

    @PostMapping("/admin/prijave/{id}/uplata")
    public PrijavaDto evidentirajUplatu(@PathVariable Long id,
                                             @Valid @RequestBody UplataZahtev zahtev) {
        return prijavaService.evidentirajUplatu(id, zahtev.getNacinPlacanja());
    }
}

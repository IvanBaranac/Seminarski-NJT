package rs.ac.bg.fon.prijemni.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.prijemni.dto.PrijavaDto;
import rs.ac.bg.fon.prijemni.dto.TerminDto;
import rs.ac.bg.fon.prijemni.dto.TerminZahtev;
import rs.ac.bg.fon.prijemni.service.PrijavaService;
import rs.ac.bg.fon.prijemni.service.TerminService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TerminKontroler {

    private static final String EXCEL_TIP =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final TerminService terminService;
    private final PrijavaService prijavaService;

    public TerminKontroler(TerminService terminService, PrijavaService prijavaService) {
        this.terminService = terminService;
        this.prijavaService = prijavaService;
    }

    @GetMapping("/termini")
    public List<TerminDto> predstojeci() {
        return terminService.predstojeci();
    }

    @GetMapping("/admin/termini")
    public List<TerminDto> svi() {
        return terminService.svi();
    }

    @PostMapping("/admin/termini")
    public ResponseEntity<TerminDto> kreiraj(@Valid @RequestBody TerminZahtev zahtev) {
        return ResponseEntity.status(HttpStatus.CREATED).body(terminService.kreiraj(zahtev));
    }

    @PutMapping("/admin/termini/{id}")
    public TerminDto izmeni(@PathVariable Long id, @Valid @RequestBody TerminZahtev zahtev) {
        return terminService.izmeni(id, zahtev);
    }

    @DeleteMapping("/admin/termini/{id}")
    public ResponseEntity<Void> obrisi(@PathVariable Long id) {
        terminService.obrisi(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/termini/{id}/kandidati")
    public List<PrijavaDto> kandidati(@PathVariable Long id) {
        return prijavaService.prijavljeniZaTermin(id);
    }

    @GetMapping("/admin/termini/{id}/kandidati/excel")
    public ResponseEntity<byte[]> kandidatiExcel(@PathVariable Long id) {
        byte[] excel = prijavaService.spisakExcel(terminService.nadji(id));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(EXCEL_TIP))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=spisak-" + id + ".xlsx")
                .body(excel);
    }
}

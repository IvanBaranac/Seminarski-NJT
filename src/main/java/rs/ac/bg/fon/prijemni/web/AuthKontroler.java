package rs.ac.bg.fon.prijemni.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.prijemni.dto.KorisnikDto;
import rs.ac.bg.fon.prijemni.dto.LoginOdgovor;
import rs.ac.bg.fon.prijemni.dto.LoginZahtev;
import rs.ac.bg.fon.prijemni.dto.RegistracijaZahtev;
import rs.ac.bg.fon.prijemni.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthKontroler {

    private final AuthService authService;

    public AuthKontroler(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registracija")
    public ResponseEntity<LoginOdgovor> registracija(@Valid @RequestBody RegistracijaZahtev zahtev) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registruj(zahtev));
    }

    @PostMapping("/login")
    public LoginOdgovor login(@Valid @RequestBody LoginZahtev zahtev) {
        return authService.prijavi(zahtev);
    }

    @GetMapping("/ja")
    public KorisnikDto ja(@AuthenticationPrincipal String email) {
        return AuthService.uDto(authService.nadjiPoEmailu(email));
    }
}

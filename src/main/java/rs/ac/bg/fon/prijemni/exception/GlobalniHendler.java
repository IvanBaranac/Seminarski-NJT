package rs.ac.bg.fon.prijemni.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalniHendler {

    @ExceptionHandler(PoslovnaGreska.class)
    public ResponseEntity<Map<String, String>> poslovnaGreska(PoslovnaGreska greska) {
        return ResponseEntity.badRequest().body(Map.of("poruka", greska.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> neispravniPodaci(MethodArgumentNotValidException greska) {
        List<FieldError> greskePolja = greska.getBindingResult().getFieldErrors();
        String poruka = "Proverite unete podatke.";

        if (!greskePolja.isEmpty()) {
            poruka = greskePolja.get(0).getDefaultMessage();
        }

        return ResponseEntity.badRequest().body(Map.of("poruka", poruka));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> ostaleGreske(Exception greska) {
        greska.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("poruka", "Došlo je do greške na serveru."));
    }
}

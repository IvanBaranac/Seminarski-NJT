package rs.ac.bg.fon.prijemni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class PrijemniApplication {

    private static final Logger log = LoggerFactory.getLogger(PrijemniApplication.class);

    @Value("${server.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(PrijemniApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void otvoriPregledac() {
        String adresa = "http://localhost:" + port;

        log.info("====================================================");
        log.info("  Aplikacija radi na: {}", adresa);
        log.info("  ADMIN:    admin@fon.bg.ac.rs / admin123");
        log.info("  KANDIDAT: pera@primer.rs / kandidat123");
        log.info("====================================================");

        try {
            Desktop.getDesktop().browse(new URI(adresa));
        } catch (Exception e) {
            log.info("Otvorite {} rucno u pregledacu.", adresa);
        }
    }
}

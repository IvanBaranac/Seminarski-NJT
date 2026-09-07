package rs.ac.bg.fon.prijemni.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.prijemni.domain.Prijava;
import rs.ac.bg.fon.prijemni.domain.StavkaPrijave;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender posiljalac;
    private final DokumentService dokumentService;
    private final boolean ukljucen;
    private final String adresaPosiljaoca;

    public EmailService(JavaMailSender posiljalac,
                        DokumentService dokumentService,
                        @Value("${app.mail.ukljucen}") boolean ukljucen,
                        @Value("${spring.mail.username}") String adresaPosiljaoca) {
        this.posiljalac = posiljalac;
        this.dokumentService = dokumentService;
        this.ukljucen = ukljucen;
        this.adresaPosiljaoca = adresaPosiljaoca;
    }

    public void posaljiUplatnicu(Prijava prijava) {
        StringBuilder telo = new StringBuilder();

        telo.append("<p>Poštovani/a ").append(prijava.getKandidat().getIme()).append(",</p>");
        telo.append("<p>Vaša prijava je evidentirana. Prijavljeni termini:</p><ul>");

        for (StavkaPrijave stavka : prijava.getStavke()) {
            telo.append("<li>").append(DokumentService.opisTermina(stavka.getTermin())).append("</li>");
        }

        telo.append("</ul>");
        telo.append("<p><b>Za uplatu: ").append(prijava.getUkupnaCena()).append(" RSD</b><br>");
        telo.append("Poziv na broj (model 97): <b>").append(prijava.getPozivNaBroj()).append("</b></p>");
        telo.append("<p>Uplatnica je u prilogu. Prijava postaje važeća kada služba evidentira uplatu.</p>");

        posalji(prijava.getKandidat().getEmail(),
                "Prijava br. " + prijava.getId() + " - uplatnica",
                telo.toString(),
                dokumentService.uplatnicaPdf(prijava),
                "uplatnica-" + prijava.getId() + ".pdf");
    }

    public void posaljiPotvrduUplate(Prijava prijava) {
        String telo = "<p>Poštovani/a " + prijava.getKandidat().getIme() + ",</p>"
                + "<p>Vaša uplata je evidentirana i status prijave je sada <b>prijavljen</b>. "
                + "Nalazite se na spisku kandidata.</p>"
                + "<p>Na ispit ponesite ličnu kartu i pribor za pisanje.</p>";

        posalji(prijava.getKandidat().getEmail(),
                "Prijava br. " + prijava.getId() + " - uplata evidentirana",
                telo, null, null);
    }

    private void posalji(String primalac, String naslov, String telo, byte[] prilog, String nazivPriloga) {
        if (!ukljucen) {
            log.info("MEJL ZA {} | {} | {}", primalac, naslov, telo.replaceAll("<[^>]+>", " "));
            return;
        }

        try {
            MimeMessage poruka = posiljalac.createMimeMessage();
            MimeMessageHelper pomocnik = new MimeMessageHelper(poruka, true, "UTF-8");

            pomocnik.setFrom(adresaPosiljaoca);
            pomocnik.setTo(primalac);
            pomocnik.setSubject(naslov);
            pomocnik.setText(telo, true);

            if (prilog != null) {
                pomocnik.addAttachment(nazivPriloga, new ByteArrayResource(prilog));
            }

            posiljalac.send(poruka);
            log.info("Mejl poslat na {}", primalac);

        } catch (Exception greska) {
            log.error("Slanje mejla nije uspelo: {}", greska.getMessage());
        }
    }
}

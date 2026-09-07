package rs.ac.bg.fon.prijemni.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.prijemni.domain.Prijava;
import rs.ac.bg.fon.prijemni.domain.StavkaPrijave;
import rs.ac.bg.fon.prijemni.domain.Termin;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DokumentService {

    private static final DateTimeFormatter DATUM = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    private final String racun;

    public DokumentService(@Value("${app.racun}") String racun) {
        this.racun = racun;
    }

    public byte[] uplatnicaPdf(Prijava prijava) {
        Font naslovFont = FontFactory.getFont("Helvetica", "Cp1250", 16, Font.BOLD);
        Font boldFont = FontFactory.getFont("Helvetica", "Cp1250", 10, Font.BOLD);
        Font obicanFont = FontFactory.getFont("Helvetica", "Cp1250", 10);

        ByteArrayOutputStream izlaz = new ByteArrayOutputStream();
        Document dokument = new Document();

        try {
            PdfWriter.getInstance(dokument, izlaz);
            dokument.open();

            dokument.add(new Paragraph("NALOG ZA UPLATU", naslovFont));
            dokument.add(new Paragraph("Probni prijemni ispit - Fakultet organizacionih nauka", obicanFont));
            dokument.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(2);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{35, 65});

            dodajRed(tabela, "Uplatilac", prijava.getKandidat().getPunoIme(), boldFont, obicanFont);
            dodajRed(tabela, "Svrha uplate", "Prijava na probni prijemni ispit", boldFont, obicanFont);
            dodajRed(tabela, "Primalac", "Fakultet organizacionih nauka, Beograd", boldFont, obicanFont);
            dodajRed(tabela, "Racun primaoca", racun, boldFont, obicanFont);
            dodajRed(tabela, "Model", "97", boldFont, obicanFont);
            dodajRed(tabela, "Poziv na broj", prijava.getPozivNaBroj(), boldFont, obicanFont);
            dodajRed(tabela, "Iznos", prijava.getUkupnaCena() + " RSD", boldFont, obicanFont);
            dodajRed(tabela, "Rok za uplatu", prijava.getRokZaUplatu().format(DATUM), boldFont, obicanFont);

            dokument.add(tabela);
            dokument.add(new Paragraph(" "));
            dokument.add(new Paragraph("Prijavljeni termini:", boldFont));

            for (StavkaPrijave stavka : prijava.getStavke()) {
                dokument.add(new Paragraph("- " + opisTermina(stavka.getTermin())
                        + " - " + stavka.getCena() + " RSD", obicanFont));
            }

            dokument.close();

        } catch (Exception greska) {
            throw new RuntimeException("Greška pri pravljenju PDF-a", greska);
        }

        return izlaz.toByteArray();
    }

    public byte[] spisakExcel(Termin termin, List<Prijava> prijave) {
        try (Workbook radnaSveska = new XSSFWorkbook();
             ByteArrayOutputStream izlaz = new ByteArrayOutputStream()) {

            Sheet list = radnaSveska.createSheet("Spisak kandidata");

            CellStyle podebljano = radnaSveska.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = radnaSveska.createFont();
            font.setBold(true);
            podebljano.setFont(font);

            Row naslov = list.createRow(0);
            naslov.createCell(0).setCellValue("Termin: " + opisTermina(termin));
            naslov.getCell(0).setCellStyle(podebljano);

            String[] kolone = {"Rb.", "Prezime i ime", "Mejl", "Telefon", "Srednja škola", "Poziv na broj"};
            Row zaglavlje = list.createRow(2);

            for (int i = 0; i < kolone.length; i++) {
                Cell celija = zaglavlje.createCell(i);
                celija.setCellValue(kolone[i]);
                celija.setCellStyle(podebljano);
            }

            int red = 3;

            for (Prijava prijava : prijave) {
                Row podaci = list.createRow(red);
                podaci.createCell(0).setCellValue(red - 2);
                podaci.createCell(1).setCellValue(prijava.getKandidat().getPrezime()
                        + " " + prijava.getKandidat().getIme());
                podaci.createCell(2).setCellValue(prijava.getKandidat().getEmail());
                podaci.createCell(3).setCellValue(tekst(prijava.getKandidat().getTelefon()));
                podaci.createCell(4).setCellValue(tekst(prijava.getKandidat().getSrednjaSkola()));
                podaci.createCell(5).setCellValue(prijava.getPozivNaBroj());
                red = red + 1;
            }

            for (int i = 0; i < kolone.length; i++) {
                list.autoSizeColumn(i);
            }

            radnaSveska.write(izlaz);
            return izlaz.toByteArray();

        } catch (Exception greska) {
            throw new RuntimeException("Greška pri pravljenju Excel fajla", greska);
        }
    }

    public static String opisTermina(Termin termin) {
        return termin.getVrstaIspita().getNaziv()
                + ", " + termin.getDatum().format(DATUM)
                + " u " + termin.getVremePocetka()
                + " (" + termin.getAdresa() + ")";
    }

    private void dodajRed(PdfPTable tabela, String oznaka, String vrednost, Font bold, Font obican) {
        PdfPCell levo = new PdfPCell(new Paragraph(oznaka, bold));
        PdfPCell desno = new PdfPCell(new Paragraph(vrednost, obican));
        levo.setPadding(6);
        desno.setPadding(6);
        tabela.addCell(levo);
        tabela.addCell(desno);
    }

    private String tekst(String vrednost) {
        if (vrednost == null) {
            return "";
        }

        return vrednost;
    }
}

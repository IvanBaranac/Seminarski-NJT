package rs.ac.bg.fon.prijemni.dto;

import java.math.BigDecimal;

public class StavkaDto {

    private Long terminId;
    private String opis;
    private BigDecimal cena;

    public StavkaDto() {
    }

    public StavkaDto(Long terminId, String opis, BigDecimal cena) {
        this.terminId = terminId;
        this.opis = opis;
        this.cena = cena;
    }

    public Long getTerminId() { return terminId; }
    public void setTerminId(Long terminId) { this.terminId = terminId; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public BigDecimal getCena() { return cena; }
    public void setCena(BigDecimal cena) { this.cena = cena; }
}

package rs.ac.bg.fon.prijemni.dto;

import jakarta.validation.constraints.NotBlank;

public class UplataZahtev {

    @NotBlank(message = "Način plaćanja je obavezan")
    private String nacinPlacanja;

    public String getNacinPlacanja() { return nacinPlacanja; }
    public void setNacinPlacanja(String nacinPlacanja) { this.nacinPlacanja = nacinPlacanja; }
}

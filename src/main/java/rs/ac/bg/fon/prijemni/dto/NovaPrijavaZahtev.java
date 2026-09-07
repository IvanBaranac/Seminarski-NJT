package rs.ac.bg.fon.prijemni.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class NovaPrijavaZahtev {

    @NotEmpty(message = "Izaberite bar jedan termin")
    private List<Long> terminIds;

    public List<Long> getTerminIds() { return terminIds; }
    public void setTerminIds(List<Long> terminIds) { this.terminIds = terminIds; }
}

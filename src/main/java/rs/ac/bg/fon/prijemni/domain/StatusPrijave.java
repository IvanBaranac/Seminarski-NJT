package rs.ac.bg.fon.prijemni.domain;

public enum StatusPrijave {

    CEKA_UPLATU("Čeka uplatu"),
    PRIJAVLJEN("Prijavljen"),
    OTKAZANA("Otkazana");

    private final String naziv;

    StatusPrijave(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }
}

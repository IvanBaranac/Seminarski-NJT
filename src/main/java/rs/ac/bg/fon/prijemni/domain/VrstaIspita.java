package rs.ac.bg.fon.prijemni.domain;

public enum VrstaIspita {

    MATEMATIKA("Matematika"),
    OPSTA_INFORMISANOST("Test opšte informisanosti");

    private final String naziv;

    VrstaIspita(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }
}

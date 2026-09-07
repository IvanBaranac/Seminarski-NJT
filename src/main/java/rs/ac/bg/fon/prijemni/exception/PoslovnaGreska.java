package rs.ac.bg.fon.prijemni.exception;

public class PoslovnaGreska extends RuntimeException {

    public PoslovnaGreska(String poruka) {
        super(poruka);
    }
}

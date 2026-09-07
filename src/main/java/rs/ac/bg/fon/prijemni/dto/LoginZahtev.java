package rs.ac.bg.fon.prijemni.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginZahtev {

    @NotBlank(message = "Mejl je obavezan")
    private String email;

    @NotBlank(message = "Lozinka je obavezna")
    private String lozinka;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLozinka() { return lozinka; }
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }
}

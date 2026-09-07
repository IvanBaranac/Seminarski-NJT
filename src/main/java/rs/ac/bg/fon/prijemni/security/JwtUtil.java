package rs.ac.bg.fon.prijemni.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey kljuc;
    private final long trajanjeMilisekundi;

    public JwtUtil(@Value("${app.jwt.tajna}") String tajna,
                   @Value("${app.jwt.sati}") long sati) {
        this.kljuc = Keys.hmacShaKeyFor(tajna.getBytes(StandardCharsets.UTF_8));
        this.trajanjeMilisekundi = sati * 60 * 60 * 1000;
    }

    public String napraviToken(String email, String uloga) {
        Date sada = new Date();

        return Jwts.builder()
                .subject(email)
                .claim("uloga", uloga)
                .issuedAt(sada)
                .expiration(new Date(sada.getTime() + trajanjeMilisekundi))
                .signWith(kljuc)
                .compact();
    }

    public Claims procitaj(String token) {
        return Jwts.parser()
                .verifyWith(kljuc)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

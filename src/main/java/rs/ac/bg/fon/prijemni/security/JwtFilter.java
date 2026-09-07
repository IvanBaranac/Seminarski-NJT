package rs.ac.bg.fon.prijemni.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest zahtev,
                                    HttpServletResponse odgovor,
                                    FilterChain lanac) throws ServletException, IOException {

        String zaglavlje = zahtev.getHeader("Authorization");

        if (zaglavlje != null && zaglavlje.startsWith("Bearer ")) {
            try {
                Claims podaci = jwtUtil.procitaj(zaglavlje.substring(7));
                String email = podaci.getSubject();
                String uloga = podaci.get("uloga", String.class);

                List<SimpleGrantedAuthority> ovlascenja = new ArrayList<>();
                ovlascenja.add(new SimpleGrantedAuthority("ROLE_" + uloga));

                UsernamePasswordAuthenticationToken prijavljen =
                        new UsernamePasswordAuthenticationToken(email, null, ovlascenja);

                SecurityContextHolder.getContext().setAuthentication(prijavljen);
            } catch (Exception greska) {
                SecurityContextHolder.clearContext();
            }
        }

        lanac.doFilter(zahtev, odgovor);
    }
}

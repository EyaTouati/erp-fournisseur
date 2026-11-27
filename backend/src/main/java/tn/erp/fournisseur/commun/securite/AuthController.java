package tn.erp.fournisseur.commun.securite;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import tn.erp.fournisseur.commun.erreurs.ErreurDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public record LoginDto(String username, String password) { }
    public record UtilisateurDto(String username, String role) { }

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository contextes = new HttpSessionSecurityContextRepository();

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/csrf") // appelé au démarrage du frontend : pose le cookie
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto, HttpServletRequest req,
                                   HttpServletResponse res) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(dto.username(), dto.password()));
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(auth);
            SecurityContextHolder.setContext(ctx);
            contextes.saveContext(ctx, req, res); // la session garde l'utilisateur
            return ResponseEntity.ok(versDto(auth));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErreurDto("CONNEXION", "Nom d'utilisateur ou mot de passe incorrect"));
        }
    }

    @GetMapping("/me")
    public UtilisateurDto me(Authentication auth) {
        return versDto(auth);
    }

    private UtilisateurDto versDto(Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return new UtilisateurDto(auth.getName(), role);
    }
}
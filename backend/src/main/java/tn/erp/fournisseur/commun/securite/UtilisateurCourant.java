package tn.erp.fournisseur.commun.securite;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utilisateur connecté, injecté dans les services de A, B, C et D.
 * nom() = nom de connexion (achats, directeur...) ; role() = ACHATS, DIRECTEUR... (sans ROLE_).
 */
@Component
public class UtilisateurCourant {

    public String nom() {
        return auth().getName();
    }

    public String role() {
        return auth().getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
    }

    private Authentication auth() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null) {
            throw new IllegalStateException("Aucun utilisateur connecté");
        }
        return a;
    }
}
package tn.erp.fournisseur.commun.workflow;

import java.time.LocalDateTime;

public record HistoriqueDto(String ancienStatut, String nouveauStatut, String utilisateur,
                            LocalDateTime dateChangement, String commentaire) {

    public static HistoriqueDto de(HistoriqueStatut h) {
        return new HistoriqueDto(h.getAncienStatut(), h.getNouveauStatut(), h.getUtilisateur(),
                h.getDateChangement(), h.getCommentaire());
    }
}
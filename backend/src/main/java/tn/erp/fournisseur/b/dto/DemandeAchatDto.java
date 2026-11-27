package tn.erp.fournisseur.b.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import tn.erp.fournisseur.b.entity.DemandeAchat;

public record DemandeAchatDto(Long id, String numero, Long produitId, String produitLibelle,
                              Integer quantite, LocalDate dateSouhaitee, String statut,
                              String motifRefus, String creePar, LocalDateTime dateCreation) {
    public static DemandeAchatDto de(DemandeAchat d, String produitLibelle) {
        return new DemandeAchatDto(d.getId(), d.getNumero(), d.getProduitId(), produitLibelle,
                d.getQuantite(), d.getDateSouhaitee(), d.getStatut().name(),
                d.getMotifRefus(), d.getCreePar(), d.getDateCreation());
    }
}
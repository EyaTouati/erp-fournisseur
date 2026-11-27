package tn.erp.fournisseur.c.dto;

import tn.erp.fournisseur.c.entity.Commande;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

public record CommandeDto(Long id, String numero, String statut, Long offreId, Long fournisseurId,
                          BigDecimal montantTotal, String creePar, String validePar, String motifRefus,
                          LocalDateTime dateCreation, LocalDateTime dateValidation, LocalDateTime dateEnvoi,
                          List<LigneCommandeDto> lignes) {

    /** libelle : donne le libellé d'un produit à partir de son id. */
    public static CommandeDto de(Commande c, Function<Long, String> libelle) {
        List<LigneCommandeDto> lignes = c.getLignes().stream()
                .map(l -> new LigneCommandeDto(l.getId(), l.getProduitId(), libelle.apply(l.getProduitId()),
                        l.getQuantite(), l.getPrixUnitaire(), l.getQuantiteRecue()))
                .toList();
        return new CommandeDto(c.getId(), c.getNumero(), c.getStatut().name(), c.getOffreId(), c.getFournisseurId(),
                c.getMontantTotal(), c.getCreePar(), c.getValidePar(), c.getMotifRefus(),
                c.getDateCreation(), c.getDateValidation(), c.getDateEnvoi(), lignes);
    }
}
package tn.erp.fournisseur.b.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import tn.erp.fournisseur.b.entity.Offre;

public record OffreDto(Long id, Long demandePrixId, Long fournisseurId, BigDecimal prixUnitaire,
                       Integer quantite, Integer delaiJours, String conditions,
                       LocalDate dateValidite, String statut, String justification,
                       LocalDateTime dateReception, boolean moinsChere, boolean expiree) {
    public static OffreDto de(Offre o, boolean moinsChere, boolean expiree) {
        return new OffreDto(o.getId(), o.getDemandePrix().getId(), o.getFournisseurId(),
                o.getPrixUnitaire(), o.getQuantite(), o.getDelaiJours(), o.getConditions(),
                o.getDateValidite(), o.getStatut().name(), o.getJustification(),
                o.getDateReception(), moinsChere, expiree);
    }
}
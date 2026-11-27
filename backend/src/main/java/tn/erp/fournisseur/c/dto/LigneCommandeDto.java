package tn.erp.fournisseur.c.dto;

import java.math.BigDecimal;

public record LigneCommandeDto(Long id, Long produitId, String produitLibelle, Integer quantite,
                               BigDecimal prixUnitaire, Integer quantiteRecue) {
}
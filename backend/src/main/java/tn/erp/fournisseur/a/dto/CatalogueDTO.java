package tn.erp.fournisseur.a.dto;

import tn.erp.fournisseur.a.entity.Catalogue;
import tn.erp.fournisseur.commun.integration.Produit;

import java.math.BigDecimal;

public record CatalogueDto(Long id, Long fournisseurId, Long produitId, String produitReference,
                           String produitLibelle, BigDecimal prixUnitaire, Integer delaiJours) {
    public static CatalogueDto de(Catalogue c, Produit p) {
        return new CatalogueDto(c.getId(), c.getFournisseur().getId(), c.getProduitId(),
                p == null ? null : p.getReference(), p == null ? null : p.getLibelle(),
                c.getPrixUnitaire(), c.getDelaiJours());
    }
}

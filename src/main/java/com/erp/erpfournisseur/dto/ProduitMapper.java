package com.erp.erpfournisseur.dto;

import com.erp.erpfournisseur.catalogue.Produit;

public class ProduitMapper {

    public static ProduitDTO toDTO(Produit produit) {
        ProduitDTO dto = new ProduitDTO();

        dto.setId(produit.getId());
        dto.setReference(produit.getReference());
        dto.setLibelle(produit.getLibelle());
        dto.setUnite(produit.getUnite());
        dto.setStockActuel(produit.getStockActuel());
        dto.setSeuilReappro(produit.getSeuilReappro());

        return dto;
    }

    public static Produit toEntity(ProduitDTO dto) {
        Produit produit = new Produit();

        produit.setReference(dto.getReference());
        produit.setLibelle(dto.getLibelle());
        produit.setUnite(dto.getUnite());
        produit.setStockActuel(dto.getStockActuel());
        produit.setSeuilReappro(dto.getSeuilReappro());

        return produit;
    }
}
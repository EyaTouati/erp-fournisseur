package tn.erp.fournisseur.a.dto;

import tn.erp.fournisseur.a.catalogue.Catalogue;

public class CatalogueMapper {

    public static CatalogueDTO toDTO(Catalogue catalogue) {
        CatalogueDTO dto = new CatalogueDTO();

        dto.setId(catalogue.getId());
        dto.setFournisseurId(catalogue.getFournisseur().getId());
        dto.setProduitId(catalogue.getProduit().getId());
        dto.setPrixUnitaire(catalogue.getPrixUnitaire());
        dto.setDelaiJours(catalogue.getDelaiJours());

        return dto;
    }
}
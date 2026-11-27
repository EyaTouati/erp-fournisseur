package tn.erp.fournisseur.c.dto;

public record LigneReceptionDto(Long id, Long ligneCommandeId, Long produitId, Integer quantiteRecue, Boolean conforme) {
}
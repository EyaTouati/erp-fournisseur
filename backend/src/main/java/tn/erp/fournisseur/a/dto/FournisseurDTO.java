package tn.erp.fournisseur.a.dto;

import tn.erp.fournisseur.a.entity.Fournisseur;

import java.time.LocalDateTime;

public record FournisseurDto(Long id, String code, String nom, String identifiantFiscal, String adresse,
                             String telephone, String email, String conditionsPaiement, String statut,
                             String motifDesactivation, LocalDateTime dateCreation) {
    public static FournisseurDto de(Fournisseur f) {
        return new FournisseurDto(f.getId(), f.getCode(), f.getNom(), f.getIdentifiantFiscal(), f.getAdresse(),
                f.getTelephone(), f.getEmail(), f.getConditionsPaiement(), f.getStatut().name(),
                f.getMotifDesactivation(), f.getDateCreation());
    }
}

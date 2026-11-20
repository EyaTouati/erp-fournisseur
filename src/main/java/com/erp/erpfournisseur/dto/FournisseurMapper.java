package com.erp.erpfournisseur.dto;

import com.erp.erpfournisseur.fournisseur.Fournisseur;

public class FournisseurMapper {

    public static FournisseurDTO toDTO(Fournisseur fournisseur) {
        FournisseurDTO dto = new FournisseurDTO();

        dto.setId(fournisseur.getId());
        dto.setNom(fournisseur.getNom());
        dto.setIdentifiantFiscal(fournisseur.getIdentifiantFiscal());
        dto.setEmail(fournisseur.getEmail());
        dto.setTelephone(fournisseur.getTelephone());
        dto.setAdresse(fournisseur.getAdresse());
        dto.setActif(fournisseur.getActif());
        dto.setMotifDesactivation(fournisseur.getMotifDesactivation());

        return dto;
    }

    public static Fournisseur toEntity(FournisseurDTO dto) {
        Fournisseur fournisseur = new Fournisseur();

        fournisseur.setNom(dto.getNom());
        fournisseur.setIdentifiantFiscal(dto.getIdentifiantFiscal());
        fournisseur.setEmail(dto.getEmail());
        fournisseur.setTelephone(dto.getTelephone());
        fournisseur.setAdresse(dto.getAdresse());
        fournisseur.setMotifDesactivation(dto.getMotifDesactivation());
        if (dto.getActif() != null) {
            fournisseur.setActif(dto.getActif());
        }

        return fournisseur;
    }
}
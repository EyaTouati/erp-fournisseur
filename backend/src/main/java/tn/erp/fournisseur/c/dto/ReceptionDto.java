package tn.erp.fournisseur.c.dto;

import tn.erp.fournisseur.c.entity.Reception;

import java.time.LocalDateTime;
import java.util.List;

public record ReceptionDto(Long id, String numero, Long commandeId, String statut, String receptionnePar,
                           LocalDateTime dateReception, String commentaire, List<LigneReceptionDto> lignes) {

    public static ReceptionDto de(Reception r) {
        List<LigneReceptionDto> lignes = r.getLignes().stream()
                .map(l -> new LigneReceptionDto(l.getId(), l.getLigneCommande().getId(),
                        l.getLigneCommande().getProduitId(), l.getQuantiteRecue(), l.getConforme()))
                .toList();
        return new ReceptionDto(r.getId(), r.getNumero(), r.getCommande().getId(), r.getStatut().name(),
                r.getReceptionnePar(), r.getDateReception(), r.getCommentaire(), lignes);
    }
}
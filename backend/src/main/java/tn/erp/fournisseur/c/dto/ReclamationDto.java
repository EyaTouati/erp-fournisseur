package tn.erp.fournisseur.c.dto;

import tn.erp.fournisseur.c.entity.Reclamation;

import java.time.LocalDateTime;

public record ReclamationDto(Long id, Long receptionId, String receptionNumero, String motif, String statut,
                             LocalDateTime dateReclamation) {

    public static ReclamationDto de(Reclamation r) {
        return new ReclamationDto(r.getId(), r.getReception().getId(), r.getReception().getNumero(),
                r.getMotif(), r.getStatut().name(), r.getDateReclamation());
    }
}
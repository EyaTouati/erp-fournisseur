package tn.erp.fournisseur.a.dto;

import tn.erp.fournisseur.a.entity.HistoriquePrix;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoriquePrixDto(Long id, Long catalogueId, BigDecimal ancienPrix, BigDecimal nouveauPrix,
                                LocalDateTime dateModification, String modifiePar) {
    public static HistoriquePrixDto de(HistoriquePrix h) {
        return new HistoriquePrixDto(h.getId(), h.getCatalogue().getId(), h.getAncienPrix(), h.getNouveauPrix(),
                h.getDateModification(), h.getModifiePar());
    }
}

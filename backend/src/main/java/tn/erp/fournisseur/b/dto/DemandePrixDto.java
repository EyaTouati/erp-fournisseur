package tn.erp.fournisseur.b.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import tn.erp.fournisseur.b.entity.DemandePrix;

public record DemandePrixDto(Long id, String numero, Long demandeAchatId, LocalDate dateLimite,
                             String statut, LocalDateTime dateEnvoi, List<Long> fournisseurIds) {
    public static DemandePrixDto de(DemandePrix d) {
        return new DemandePrixDto(d.getId(), d.getNumero(), d.getDemandeAchat().getId(),
                d.getDateLimite(), d.getStatut().name(), d.getDateEnvoi(),
                d.getFournisseurIds().stream().sorted().toList());
    }
}
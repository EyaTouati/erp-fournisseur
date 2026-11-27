package tn.erp.fournisseur.c.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record LigneReceptionCreationDto(@NotNull Long ligneCommandeId,
                                        @NotNull @PositiveOrZero Integer quantiteRecue,
                                        Boolean conforme) {
}
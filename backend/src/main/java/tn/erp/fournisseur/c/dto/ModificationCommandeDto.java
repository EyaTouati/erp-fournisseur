package tn.erp.fournisseur.c.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ModificationCommandeDto(@NotEmpty List<LigneQuantiteDto> lignes) {
}
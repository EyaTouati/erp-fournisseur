package tn.erp.fournisseur.c.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReceptionCreationDto(@NotEmpty List<@Valid LigneReceptionCreationDto> lignes, String commentaire) {
}
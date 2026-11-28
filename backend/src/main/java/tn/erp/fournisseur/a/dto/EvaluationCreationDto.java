package tn.erp.fournisseur.a.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EvaluationCreationDto(Long commandeId,
                                    @NotNull @Min(1) @Max(5) Integer noteDelai,
                                    @NotNull @Min(1) @Max(5) Integer noteQualite,
                                    @NotNull @Min(1) @Max(5) Integer notePrix,
                                    String commentaire) { }

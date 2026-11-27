package tn.erp.fournisseur.a.dto;

import jakarta.validation.constraints.NotBlank;

public record FournisseurCreationDto(@NotBlank String nom, @NotBlank String identifiantFiscal, String adresse,
                                     String telephone, String email, String conditionsPaiement) { }

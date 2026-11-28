package tn.erp.fournisseur.a.dto;

import jakarta.validation.constraints.NotBlank;

public record MotifDto(@NotBlank String motif) { }

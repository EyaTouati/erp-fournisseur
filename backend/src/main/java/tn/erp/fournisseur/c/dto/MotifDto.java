package tn.erp.fournisseur.c.dto;

import jakarta.validation.constraints.NotBlank;

public record MotifDto(@NotBlank String motif) {
}
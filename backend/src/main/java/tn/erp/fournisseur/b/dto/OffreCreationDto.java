package tn.erp.fournisseur.b.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record OffreCreationDto(@NotNull Long fournisseurId,
                               @NotNull @Positive BigDecimal prixUnitaire,
                               @NotNull @Positive Integer quantite,
                               @NotNull @PositiveOrZero Integer delaiJours,
                               @Size(max = 255) String conditions,
                               @NotNull LocalDate dateValidite) { }
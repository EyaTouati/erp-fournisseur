package tn.erp.fournisseur.a.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CatalogueCreationDto(@NotNull Long produitId, @NotNull @Positive BigDecimal prixUnitaire,
                                   @NotNull @PositiveOrZero Integer delaiJours) { }

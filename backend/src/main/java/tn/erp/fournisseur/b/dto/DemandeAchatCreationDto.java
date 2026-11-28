package tn.erp.fournisseur.b.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record DemandeAchatCreationDto(@NotNull Long produitId,
                                      @NotNull @Positive Integer quantite,
                                      LocalDate dateSouhaitee) { }
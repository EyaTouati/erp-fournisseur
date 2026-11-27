package tn.erp.fournisseur.b.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record DemandePrixCreationDto(@NotNull Long demandeAchatId,
                                     @NotNull LocalDate dateLimite,
                                     @NotNull @Size(min = 2) List<Long> fournisseurIds) { }
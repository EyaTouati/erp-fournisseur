package tn.erp.fournisseur.b.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.erp.fournisseur.b.dto.DemandePrixCreationDto;
import tn.erp.fournisseur.b.dto.DemandePrixDto;
import tn.erp.fournisseur.b.service.DemandePrixService;

@RestController
@RequestMapping("/api/b")
public class DemandePrixController {

    private final DemandePrixService service;

    public DemandePrixController(DemandePrixService service) { this.service = service; }

    @PostMapping("/demandes-prix")
    @PreAuthorize("hasRole('ACHATS')")
    @ResponseStatus(HttpStatus.CREATED)
    public DemandePrixDto creer(@Valid @RequestBody DemandePrixCreationDto dto) {
        return service.creer(dto);
    }

    @GetMapping("/demandes-prix/{id}")
    @PreAuthorize("hasRole('ACHATS')")
    public DemandePrixDto obtenir(@PathVariable Long id) {
        return service.obtenir(id);
    }

    @PostMapping("/demandes-prix/{id}/envoyer")
    @PreAuthorize("hasRole('ACHATS')")
    public DemandePrixDto envoyer(@PathVariable Long id) {
        return service.envoyer(id);
    }
}
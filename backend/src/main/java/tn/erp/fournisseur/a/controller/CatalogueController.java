package tn.erp.fournisseur.a.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.erp.fournisseur.a.dto.CatalogueCreationDto;
import tn.erp.fournisseur.a.dto.CatalogueDto;
import tn.erp.fournisseur.a.dto.HistoriquePrixDto;
import tn.erp.fournisseur.a.dto.PrixDto;
import tn.erp.fournisseur.a.service.CatalogueService;

import java.util.List;

@RestController
@RequestMapping("/api/a")
public class CatalogueController {

    private final CatalogueService service;

    public CatalogueController(CatalogueService service) {
        this.service = service;
    }

    @GetMapping("/fournisseurs/{id}/catalogue")
    @PreAuthorize("hasRole('ACHATS')")
    public List<CatalogueDto> lister(@PathVariable Long id) {
        return service.lister(id);
    }

    @PostMapping("/fournisseurs/{id}/catalogue")
    @PreAuthorize("hasRole('ACHATS')")
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogueDto ajouter(@PathVariable Long id, @Valid @RequestBody CatalogueCreationDto dto) {
        return service.ajouter(id, dto);
    }

    @PutMapping("/catalogue/{id}")
    @PreAuthorize("hasRole('ACHATS')")
    public CatalogueDto modifierPrix(@PathVariable Long id, @Valid @RequestBody PrixDto dto) {
        return service.modifierPrix(id, dto);
    }

    @GetMapping("/catalogue/{id}/historique")
    @PreAuthorize("hasRole('ACHATS')")
    public List<HistoriquePrixDto> historique(@PathVariable Long id) {
        return service.historique(id);
    }
}

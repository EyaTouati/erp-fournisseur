package tn.erp.fournisseur.b.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.erp.fournisseur.b.dto.OffreCreationDto;
import tn.erp.fournisseur.b.dto.OffreDto;
import tn.erp.fournisseur.b.dto.RetenirDto;
import tn.erp.fournisseur.b.service.OffreService;

@RestController
@RequestMapping("/api/b")
public class OffreController {

    private final OffreService service;

    public OffreController(OffreService service) { this.service = service; }

    @PostMapping("/demandes-prix/{id}/offres")
    @PreAuthorize("hasRole('ACHATS')")
    @ResponseStatus(HttpStatus.CREATED)
    public OffreDto saisir(@PathVariable Long id, @Valid @RequestBody OffreCreationDto dto) {
        return service.saisir(id, dto);
    }

    @GetMapping("/demandes-prix/{id}/offres")
    @PreAuthorize("hasRole('ACHATS')")
    public List<OffreDto> lister(@PathVariable Long id) {
        return service.lister(id);
    }

    @PostMapping("/offres/{id}/retenir")
    @PreAuthorize("hasRole('ACHATS')")
    public OffreDto retenir(@PathVariable Long id, @RequestBody(required = false) RetenirDto dto) {
        return service.retenir(id, dto == null ? null : dto.justification());
    }

    @PostMapping("/offres/{id}/rejeter")
    @PreAuthorize("hasRole('ACHATS')")
    public OffreDto rejeter(@PathVariable Long id) {
        return service.rejeter(id);
    }
}
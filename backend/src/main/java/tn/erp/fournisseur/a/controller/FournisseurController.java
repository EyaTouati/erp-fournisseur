package tn.erp.fournisseur.a.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.erp.fournisseur.a.dto.*;
import tn.erp.fournisseur.a.service.EvaluationService;
import tn.erp.fournisseur.a.service.FournisseurService;

import java.util.List;

@RestController
@RequestMapping("/api/a")
public class FournisseurController {

    private final FournisseurService service;
    private final EvaluationService evaluations;

    public FournisseurController(FournisseurService service, EvaluationService evaluations) {
        this.service = service;
        this.evaluations = evaluations;
    }

    @GetMapping("/fournisseurs")
    @PreAuthorize("hasAnyRole('ACHATS','DIRECTEUR')")
    public List<FournisseurDto> lister(@RequestParam(required = false) String statut,
                                       @RequestParam(required = false) String q) {
        return service.lister(statut, q);
    }

    // chemin littéral : prioritaire sur /fournisseurs/{id}
    @GetMapping("/fournisseurs/actifs")
    @PreAuthorize("hasRole('ACHATS')")
    public List<FournisseurDto> actifs(@RequestParam Long produitId) {
        return service.fournisseursActifsPourProduit(produitId).stream().map(FournisseurDto::de).toList();
    }

    @GetMapping("/fournisseurs/{id}")
    @PreAuthorize("hasAnyRole('ACHATS','DIRECTEUR')")
    public FournisseurDto trouver(@PathVariable Long id) {
        return service.trouver(id);
    }

    @PostMapping("/fournisseurs")
    @PreAuthorize("hasRole('ACHATS')")
    @ResponseStatus(HttpStatus.CREATED)
    public FournisseurDto creer(@Valid @RequestBody FournisseurCreationDto dto) {
        return service.creer(dto);
    }

    @PutMapping("/fournisseurs/{id}")
    @PreAuthorize("hasRole('ACHATS')")
    public FournisseurDto modifier(@PathVariable Long id, @Valid @RequestBody FournisseurCreationDto dto) {
        return service.modifier(id, dto);
    }

    @PostMapping("/fournisseurs/{id}/desactiver")
    @PreAuthorize("hasRole('ACHATS')")
    public FournisseurDto desactiver(@PathVariable Long id, @Valid @RequestBody MotifDto dto) {
        return service.desactiver(id, dto.motif());
    }

    @PostMapping("/fournisseurs/{id}/evaluations")
    @PreAuthorize("hasRole('ACHATS')")
    @ResponseStatus(HttpStatus.CREATED)
    public EvaluationDto evaluer(@PathVariable Long id, @Valid @RequestBody EvaluationCreationDto dto) {
        return evaluations.creer(id, dto);
    }

    @GetMapping("/fournisseurs/{id}/evaluations")
    @PreAuthorize("hasAnyRole('ACHATS','DIRECTEUR')")
    public EvaluationListeDto evaluations(@PathVariable Long id) {
        return evaluations.lister(id);
    }
}

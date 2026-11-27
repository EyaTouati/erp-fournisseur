package tn.erp.fournisseur.c.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.erp.fournisseur.c.dto.MotifDto;
import tn.erp.fournisseur.c.dto.ReceptionDto;
import tn.erp.fournisseur.c.dto.ReclamationDto;
import tn.erp.fournisseur.c.entity.StatutReclamation;
import tn.erp.fournisseur.c.service.ReceptionService;
import tn.erp.fournisseur.c.service.ReclamationService;

import java.util.List;

@RestController
@RequestMapping("/api/c")
public class ReceptionController {

    private final ReceptionService receptions;
    private final ReclamationService reclamations;

    public ReceptionController(ReceptionService receptions, ReclamationService reclamations) {
        this.receptions = receptions;
        this.reclamations = reclamations;
    }

    @GetMapping("/receptions")
    @PreAuthorize("hasAnyRole('MAGASINIER', 'ACHATS')")
    public List<ReceptionDto> receptions(@RequestParam(name = "commandeId", required = false) Long commandeId) {
        return receptions.lister(commandeId);
    }

    @PostMapping("/receptions/{id}/reclamations")
    @PreAuthorize("hasRole('MAGASINIER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReclamationDto reclamer(@PathVariable("id") Long id, @Valid @RequestBody MotifDto dto) {
        return reclamations.creer(id, dto.motif());
    }

    @GetMapping("/reclamations")
    @PreAuthorize("hasAnyRole('MAGASINIER', 'ACHATS')")
    public List<ReclamationDto> reclamations(@RequestParam(name = "statut", required = false) StatutReclamation statut) {
        return reclamations.lister(statut);
    }
}
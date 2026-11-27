package tn.erp.fournisseur.c.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.erp.fournisseur.c.dto.*;
import tn.erp.fournisseur.c.entity.StatutCommande;
import tn.erp.fournisseur.c.service.CommandeService;
import tn.erp.fournisseur.c.service.ReceptionService;

import java.util.List;

/** Section 12.5 du dossier de référence. POST /api/c/commandes {offreId} sera ajouté quand B sera prêt. */
@RestController
@RequestMapping("/api/c/commandes")
public class CommandeController {

    private final CommandeService commandes;
    private final ReceptionService receptions;

    public CommandeController(CommandeService commandes, ReceptionService receptions) {
        this.commandes = commandes;
        this.receptions = receptions;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ACHATS', 'DIRECTEUR', 'MAGASINIER')")
    public List<CommandeDto> lister(@RequestParam(name = "statut", required = false) StatutCommande statut) {
        return commandes.lister(statut);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ACHATS', 'DIRECTEUR', 'MAGASINIER')")
    public CommandeDto detail(@PathVariable("id") Long id) {
        return commandes.detail(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ACHATS')")
    public CommandeDto modifier(@PathVariable("id") Long id, @Valid @RequestBody ModificationCommandeDto dto) {
        return commandes.modifier(id, dto.lignes());
    }

    @PostMapping("/{id}/soumettre")
    @PreAuthorize("hasRole('ACHATS')")
    public CommandeDto soumettre(@PathVariable("id") Long id) {
        return commandes.soumettre(id);
    }

    @PostMapping("/{id}/valider")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public CommandeDto valider(@PathVariable("id") Long id) {
        return commandes.valider(id);
    }

    @PostMapping("/{id}/refuser")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public CommandeDto refuser(@PathVariable("id") Long id, @Valid @RequestBody MotifDto dto) {
        return commandes.refuser(id, dto.motif());
    }

    @PostMapping("/{id}/annuler")
    @PreAuthorize("hasRole('ACHATS')")
    public CommandeDto annuler(@PathVariable("id") Long id) {
        return commandes.annuler(id);
    }

    @PostMapping("/{id}/envoyer")
    @PreAuthorize("hasRole('ACHATS')")
    public CommandeDto envoyer(@PathVariable("id") Long id) {
        return commandes.envoyer(id);
    }

    @PostMapping("/{id}/receptions")
    @PreAuthorize("hasRole('MAGASINIER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReceptionDto receptionner(@PathVariable("id") Long id, @Valid @RequestBody ReceptionCreationDto dto) {
        return receptions.receptionner(id, dto);
    }
}
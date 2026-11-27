package tn.erp.fournisseur.b.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.erp.fournisseur.b.dto.DemandeAchatCreationDto;
import tn.erp.fournisseur.b.dto.DemandeAchatDto;
import tn.erp.fournisseur.b.dto.MotifDto;
import tn.erp.fournisseur.b.entity.StatutDemandeAchat;
import tn.erp.fournisseur.b.service.DemandeAchatService;

@RestController
@RequestMapping("/api/b")
public class DemandeAchatController {

    private final DemandeAchatService service;

    public DemandeAchatController(DemandeAchatService service) { this.service = service; }

    @GetMapping("/demandes")
    @PreAuthorize("hasAnyRole('MAGASINIER','ACHATS')")
    public List<DemandeAchatDto> lister(@RequestParam(required = false) StatutDemandeAchat statut) {
        return service.lister(statut);
    }

    @GetMapping("/demandes/{id}")
    @PreAuthorize("hasAnyRole('MAGASINIER','ACHATS')")
    public DemandeAchatDto obtenir(@PathVariable Long id) {
        return service.obtenir(id);
    }

    @PostMapping("/demandes")
    @PreAuthorize("hasRole('MAGASINIER')")
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeAchatDto creer(@Valid @RequestBody DemandeAchatCreationDto dto) {
        return service.creer(dto);
    }

    @PostMapping("/demandes/{id}/soumettre")
    @PreAuthorize("hasRole('MAGASINIER')")
    public DemandeAchatDto soumettre(@PathVariable Long id) {
        return service.soumettre(id);
    }

    @PostMapping("/demandes/{id}/accepter")
    @PreAuthorize("hasRole('ACHATS')")
    public DemandeAchatDto accepter(@PathVariable Long id) {
        return service.accepter(id);
    }

    @PostMapping("/demandes/{id}/refuser")
    @PreAuthorize("hasRole('ACHATS')")
    public DemandeAchatDto refuser(@PathVariable Long id, @Valid @RequestBody MotifDto dto) {
        return service.refuser(id, dto.motif());
    }
}
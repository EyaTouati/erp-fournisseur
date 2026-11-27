package tn.erp.fournisseur.commun.workflow;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** GET /api/historique/COMMANDE/12 : tous les changements de statut d'un document. */
@RestController
@RequestMapping("/api/historique")
public class HistoriqueController {

    private final WorkflowService workflow;

    public HistoriqueController(WorkflowService workflow) {
        this.workflow = workflow;
    }

    @GetMapping("/{type}/{id}")
    @PreAuthorize("isAuthenticated()")
    public List<HistoriqueDto> historique(@PathVariable("type") String type, @PathVariable("id") Long id) {
        return workflow.historique(type.toUpperCase(), id).stream().map(HistoriqueDto::de).toList();
    }
}
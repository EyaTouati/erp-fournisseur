package tn.erp.fournisseur.c.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.c.dto.ReclamationDto;
import tn.erp.fournisseur.c.entity.Reception;
import tn.erp.fournisseur.c.entity.Reclamation;
import tn.erp.fournisseur.c.entity.StatutReception;
import tn.erp.fournisseur.c.entity.StatutReclamation;
import tn.erp.fournisseur.c.repository.ReceptionRepository;
import tn.erp.fournisseur.c.repository.ReclamationRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

import java.util.List;

@Service
public class ReclamationService {

    private final ReclamationRepository reclamations;
    private final ReceptionRepository receptions;
    private final WorkflowService workflow;
    private final UtilisateurCourant utilisateur;

    public ReclamationService(ReclamationRepository reclamations, ReceptionRepository receptions,
                              WorkflowService workflow, UtilisateurCourant utilisateur) {
        this.reclamations = reclamations;
        this.receptions = receptions;
        this.workflow = workflow;
        this.utilisateur = utilisateur;
    }

    /** Réclamation supplémentaire, seulement sur une réception NON_CONFORME. */
    @Transactional
    public ReclamationDto creer(Long receptionId, String motif) {
        if (motif == null || motif.isBlank()) {
            throw new RegleGestionException("MOTIF", "Le motif de la réclamation est obligatoire");
        }
        Reception r = receptions.findById(receptionId)
                .orElseThrow(() -> new IntrouvableException("Réception " + receptionId + " introuvable"));
        if (r.getStatut() != StatutReception.NON_CONFORME) {
            throw new RegleGestionException("RECLAMATION", "Réclamation possible seulement sur une réception non conforme");
        }
        Reclamation rec = reclamations.save(new Reclamation(r, motif));
        workflow.enregistrerCreation("RECLAMATION", rec.getId(), StatutReclamation.OUVERTE.name(), utilisateur.nom());
        return ReclamationDto.de(rec);
    }

    @Transactional(readOnly = true)
    public List<ReclamationDto> lister(StatutReclamation statut) {
        List<Reclamation> liste = statut == null ? reclamations.findAllByOrderByIdDesc()
                : reclamations.findByStatutOrderByIdDesc(statut);
        return liste.stream().map(ReclamationDto::de).toList();
    }
}
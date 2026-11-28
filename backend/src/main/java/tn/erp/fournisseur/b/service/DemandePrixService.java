package tn.erp.fournisseur.b.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.a.service.FournisseurService;
import tn.erp.fournisseur.b.dto.DemandePrixCreationDto;
import tn.erp.fournisseur.b.dto.DemandePrixDto;
import tn.erp.fournisseur.b.entity.DemandeAchat;
import tn.erp.fournisseur.b.entity.DemandePrix;
import tn.erp.fournisseur.b.entity.StatutDemandeAchat;
import tn.erp.fournisseur.b.entity.StatutDemandePrix;
import tn.erp.fournisseur.b.repository.DemandeAchatRepository;
import tn.erp.fournisseur.b.repository.DemandePrixRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

@Service
public class DemandePrixService {

    private static final String TYPE = "DEMANDE_PRIX";

    private final DemandePrixRepository demandesPrix;
    private final DemandeAchatRepository demandesAchat;
    private final FournisseurService fournisseurs;
    private final WorkflowService workflow;
    private final UtilisateurCourant utilisateur;

    public DemandePrixService(DemandePrixRepository demandesPrix, DemandeAchatRepository demandesAchat,
                              FournisseurService fournisseurs, WorkflowService workflow,
                              UtilisateurCourant utilisateur) {
        this.demandesPrix = demandesPrix;
        this.demandesAchat = demandesAchat;
        this.fournisseurs = fournisseurs;
        this.workflow = workflow;
        this.utilisateur = utilisateur;
    }

    @Transactional
    public DemandePrixDto creer(DemandePrixCreationDto dto) {
        DemandeAchat da = demandesAchat.findById(dto.demandeAchatId())
                .orElseThrow(() -> new IntrouvableException(
                        "Demande d'achat " + dto.demandeAchatId() + " introuvable"));
        if (da.getStatut() != StatutDemandeAchat.ACCEPTEE)
            throw new RegleGestionException("B-ETAT", "La demande d'achat doit être acceptée");

        List<Long> ids = dto.fournisseurIds().stream().distinct().toList();
        if (ids.size() < 2)
            throw new RegleGestionException("B-FOURNISSEURS", "Au moins 2 fournisseurs différents sont nécessaires");
        for (Long id : ids) {                                   // RG-B1
            if (!fournisseurs.estActif(id))
                throw new RegleGestionException("RG-B1", "Le fournisseur " + id + " n'est pas actif");
        }
        if (!dto.dateLimite().isAfter(LocalDate.now()))
            throw new RegleGestionException("B-DATE", "La date limite doit être postérieure à aujourd'hui");

        String numero = String.format("DP-%d-%04d", java.time.Year.now().getValue(), demandesPrix.count() + 1);
        DemandePrix dp = demandesPrix.save(new DemandePrix(numero, da, dto.dateLimite(), new HashSet<>(ids)));
        workflow.enregistrerCreation(TYPE, dp.getId(), "BROUILLON", utilisateur.nom());
        return DemandePrixDto.de(dp);
    }

    @Transactional(readOnly = true)
    public DemandePrixDto obtenir(Long id) {
        return DemandePrixDto.de(trouver(id));
    }

    @Transactional
    public DemandePrixDto envoyer(Long id) {
        DemandePrix dp = trouver(id);
        workflow.changerStatut(TYPE, dp.getId(), dp.getStatut().name(), "ENVOYEE",
                utilisateur.nom(), utilisateur.role());
        dp.setStatut(StatutDemandePrix.ENVOYEE);
        dp.setDateEnvoi(LocalDateTime.now());
        return DemandePrixDto.de(dp);
    }

    private DemandePrix trouver(Long id) {
        return demandesPrix.findById(id)
                .orElseThrow(() -> new IntrouvableException("Demande de prix " + id + " introuvable"));
    }
}
package tn.erp.fournisseur.a.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.a.dto.FournisseurCreationDto;
import tn.erp.fournisseur.a.dto.FournisseurDto;
import tn.erp.fournisseur.a.entity.Fournisseur;
import tn.erp.fournisseur.a.entity.StatutFournisseur;
import tn.erp.fournisseur.a.repository.FournisseurRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

import java.util.List;

@Service
public class FournisseurService {

    private static final String TYPE = "FOURNISSEUR";

    private final FournisseurRepository fournisseurs;
    private final EvaluationService evaluations;
    private final WorkflowService workflow;
    private final UtilisateurCourant utilisateur;

    public FournisseurService(FournisseurRepository fournisseurs, EvaluationService evaluations,
                              WorkflowService workflow, UtilisateurCourant utilisateur) {
        this.fournisseurs = fournisseurs;
        this.evaluations = evaluations;
        this.workflow = workflow;
        this.utilisateur = utilisateur;
    }

    // ---------------- endpoints ----------------

    @Transactional
    public FournisseurDto creer(FournisseurCreationDto dto) {
        if (fournisseurs.existsByIdentifiantFiscal(dto.identifiantFiscal()))
            throw new RegleGestionException("RG-A1", "Identifiant fiscal déjà utilisé");
        String code = String.format("FRN-%04d", fournisseurs.count() + 1);
        Fournisseur f = fournisseurs.save(new Fournisseur(code, dto.nom(), dto.identifiantFiscal(),
                dto.adresse(), dto.telephone(), dto.email(), dto.conditionsPaiement()));
        workflow.enregistrerCreation(TYPE, f.getId(), "ACTIF", utilisateur.nom());
        return FournisseurDto.de(f);
    }

    @Transactional
    public FournisseurDto modifier(Long id, FournisseurCreationDto dto) {
        Fournisseur f = trouverEntite(id);
        if (f.getStatut() == StatutFournisseur.INACTIF)
            throw new RegleGestionException("RG-A3", "Un fournisseur inactif ne peut pas être modifié");
        if (!dto.identifiantFiscal().equals(f.getIdentifiantFiscal())
                && fournisseurs.existsByIdentifiantFiscalAndIdNot(dto.identifiantFiscal(), id))
            throw new RegleGestionException("RG-A1", "Identifiant fiscal déjà utilisé");
        f.modifier(dto.nom(), dto.identifiantFiscal(), dto.adresse(), dto.telephone(), dto.email(),
                dto.conditionsPaiement());
        return FournisseurDto.de(f);
    }

    @Transactional
    public FournisseurDto desactiver(Long id, String motif) {
        Fournisseur f = trouverEntite(id);
        if (motif == null || motif.isBlank())
            throw new RegleGestionException("RG-A5", "Le motif de désactivation est obligatoire");
        if (f.getStatut() == StatutFournisseur.INACTIF)
            throw new RegleGestionException("RG-A5", "Le fournisseur est déjà inactif");
        if (fournisseurs.compterCommandesNonCloturees(id) > 0)
            throw new RegleGestionException("RG-A6",
                    "Désactivation refusée : le fournisseur a des commandes non clôturées");
        workflow.changerStatut(TYPE, id, f.getStatut().name(), "INACTIF",
                utilisateur.nom(), utilisateur.role(), motif);
        f.desactiver(motif);
        return FournisseurDto.de(f);
    }

    @Transactional(readOnly = true)
    public List<FournisseurDto> lister(String statut, String q) {
        StatutFournisseur filtre = null;
        if (statut != null && !statut.isBlank()) {
            try {
                filtre = StatutFournisseur.valueOf(statut.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RegleGestionException("VALIDATION", "Statut inconnu : " + statut);
            }
        }
        final StatutFournisseur st = filtre;
        final String recherche = (q == null || q.isBlank()) ? null : q.trim().toLowerCase();
        return fournisseurs.findAll().stream()
                .filter(f -> st == null || f.getStatut() == st)
                .filter(f -> recherche == null
                        || f.getNom().toLowerCase().contains(recherche)
                        || f.getCode().toLowerCase().contains(recherche))
                .map(FournisseurDto::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public FournisseurDto trouver(Long id) {
        return FournisseurDto.de(trouverEntite(id));
    }

    // ---------------- contrat 4.6 (utilisé par B et C) ----------------

    @Transactional(readOnly = true)
    public List<Fournisseur> fournisseursActifsPourProduit(Long produitId) {
        return fournisseurs.findFournisseursByProduitId(produitId).stream()
                .filter(f -> f.getStatut() == StatutFournisseur.ACTIF)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean estActif(Long fournisseurId) {
        return trouverEntite(fournisseurId).getStatut() == StatutFournisseur.ACTIF;
    }

    @Transactional
    public void enregistrerEvaluation(Long fournisseurId, Long commandeId, int noteDelai, int noteQualite,
                                      int notePrix, String evaluateur) {
        evaluations.enregistrer(fournisseurId, commandeId, noteDelai, noteQualite, notePrix, null, evaluateur);
    }

    // ---------------- interne ----------------

    private Fournisseur trouverEntite(Long id) {
        return fournisseurs.findById(id)
                .orElseThrow(() -> new IntrouvableException("Fournisseur " + id + " introuvable"));
    }
}

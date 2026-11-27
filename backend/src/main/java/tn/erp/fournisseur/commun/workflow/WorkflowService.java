package tn.erp.fournisseur.commun.workflow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.TransitionInterditeException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Moteur de workflow du module Fournisseur.
 * <p>
 * Tout changement de statut d'un document (fournisseur, commande, facture...) passe par
 * {@link #changerStatut}. Le service vérifie que la transition existe dans la table
 * transition_statut et que le rôle de l'utilisateur est autorisé, puis enregistre le
 * changement dans historique_statut. Sinon il lance une {@link TransitionInterditeException}.
 * <p>
 * Utilisation dans un service métier (exemple C) :
 * <pre>
 * workflow.changerStatut("COMMANDE", commande.getId(), "EN_ATTENTE_VALIDATION", "VALIDEE",
 *                        utilisateur, "DIRECTEUR");
 * commande.setStatut(StatutCommande.VALIDEE);
 * </pre>
 */
@Service
public class WorkflowService {

    /** Rôle utilisé pour les changements faits automatiquement par un service. */
    public static final String ROLE_SYSTEME = "SYSTEME";

    private final TransitionStatutRepository transitionRepository;
    private final HistoriqueStatutRepository historiqueRepository;
    private final ParametreRepository parametreRepository;

    public WorkflowService(TransitionStatutRepository transitionRepository,
                           HistoriqueStatutRepository historiqueRepository,
                           ParametreRepository parametreRepository) {
        this.transitionRepository = transitionRepository;
        this.historiqueRepository = historiqueRepository;
        this.parametreRepository = parametreRepository;
    }

    /** Change le statut d'un document, sans commentaire. */
    @Transactional
    public HistoriqueStatut changerStatut(String typeDocument, Long documentId, String ancien, String nouveau,
                                          String utilisateur, String role) {
        return changerStatut(typeDocument, documentId, ancien, nouveau, utilisateur, role, null);
    }

    /**
     * Vérifie puis enregistre un changement de statut.
     *
     * @param typeDocument COMMANDE, FACTURE, OFFRE... (dossier de référence, section 8.2)
     * @param documentId   id du document concerné
     * @param ancien       statut actuel du document
     * @param nouveau      statut demandé
     * @param utilisateur  nom de connexion de celui qui fait l'action
     * @param role         MAGASINIER, ACHATS, DIRECTEUR, COMPTABLE ou SYSTEME
     * @param commentaire  motif éventuel (refus, litige...), peut être null
     * @return la ligne d'historique enregistrée
     * @throws TransitionInterditeException si la transition n'existe pas ou si le rôle n'est pas autorisé
     */
    @Transactional
    public HistoriqueStatut changerStatut(String typeDocument, Long documentId, String ancien, String nouveau,
                                          String utilisateur, String role, String commentaire) {
        List<TransitionStatut> transitions =
                transitionRepository.findByTypeDocumentAndStatutDepartAndStatutArrivee(typeDocument, ancien, nouveau);

        if (transitions.isEmpty()) {
            throw new TransitionInterditeException(
                    "Transition interdite pour " + typeDocument + " : " + ancien + " → " + nouveau);
        }
        boolean roleAutorise = transitions.stream().anyMatch(t -> t.getRoleAutorise().equals(role));
        if (!roleAutorise) {
            throw new TransitionInterditeException(
                    "Le rôle " + role + " ne peut pas faire passer " + typeDocument + " de " + ancien + " à " + nouveau);
        }
        return historiqueRepository.save(
                new HistoriqueStatut(typeDocument, documentId, ancien, nouveau, utilisateur, commentaire));
    }

    /** Enregistre la création d'un document (statut initial, pas de transition à vérifier). */
    @Transactional
    public HistoriqueStatut enregistrerCreation(String typeDocument, Long documentId, String statutInitial,
                                                String utilisateur) {
        return historiqueRepository.save(
                new HistoriqueStatut(typeDocument, documentId, null, statutInitial, utilisateur, null));
    }

    /** Tous les changements de statut d'un document, du plus ancien au plus récent. */
    @Transactional(readOnly = true)
    public List<HistoriqueStatut> historique(String typeDocument, Long documentId) {
        return historiqueRepository.findByTypeDocumentAndDocumentIdOrderByDateChangementAsc(typeDocument, documentId);
    }

    /** Valeur texte d'un paramètre (table parametre). */
    @Transactional(readOnly = true)
    public String parametre(String cle) {
        return parametreRepository.findById(cle)
                .map(Parametre::getValeur)
                .orElseThrow(() -> new IntrouvableException("Paramètre introuvable : " + cle));
    }

    /** Valeur numérique d'un paramètre, ex. parametreDecimal("seuil_validation") pour RG-C2. */
    @Transactional(readOnly = true)
    public BigDecimal parametreDecimal(String cle) {
        return new BigDecimal(parametre(cle).trim());
    }
}

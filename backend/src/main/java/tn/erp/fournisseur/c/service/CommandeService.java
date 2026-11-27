package tn.erp.fournisseur.c.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.c.dto.CommandeDto;
import tn.erp.fournisseur.c.dto.LigneQuantiteDto;
import tn.erp.fournisseur.c.entity.Commande;
import tn.erp.fournisseur.c.entity.LigneCommande;
import tn.erp.fournisseur.c.entity.StatutCommande;
import tn.erp.fournisseur.c.repository.CommandeRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.integration.Produit;
import tn.erp.fournisseur.commun.integration.StockService;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

/**
 * Sous-processus C : bon de commande et validation (RG-C1, C2, C3, C5),
 * plus les méthodes du contrat utilisées par D (section 4.6).
 */
@Service
public class CommandeService {

    private static final String TYPE = "COMMANDE";

    private final CommandeRepository commandes;
    private final WorkflowService workflow;
    private final UtilisateurCourant utilisateur;
    private final StockService stock;

    public CommandeService(CommandeRepository commandes, WorkflowService workflow,
                           UtilisateurCourant utilisateur, StockService stock) {
        this.commandes = commandes;
        this.workflow = workflow;
        this.utilisateur = utilisateur;
        this.stock = stock;
    }

    /**
     * RG-C1 : crée le bon de commande à partir des données de l'offre retenue.
     * Quand B sera prêt, creer(offreId) lira l'offre avec OffreService.getOffreRetenue puis appellera cette méthode.
     */
    @Transactional
    public CommandeDto creerDepuisOffre(Long offreId, Long fournisseurId, Long produitId, int quantite,
                                        BigDecimal prixUnitaire) {
        if (commandes.existsByOffreId(offreId)) {
            throw new RegleGestionException("RG-C1", "Une commande existe déjà pour cette offre");
        }
        String numero = String.format("BC-%d-%04d", Year.now().getValue(), commandes.count() + 1);
        Commande c = new Commande(numero, offreId, fournisseurId, utilisateur.nom());
        c.ajouterLigne(produitId, quantite, prixUnitaire);
        c = commandes.save(c);
        workflow.enregistrerCreation(TYPE, c.getId(), StatutCommande.BROUILLON.name(), utilisateur.nom());
        return versDto(c);
    }

    /** RG-C5 : modification des quantités seulement en BROUILLON. */
    @Transactional
    public CommandeDto modifier(Long id, List<LigneQuantiteDto> lignes) {
        Commande c = trouver(id);
        if (c.getStatut() != StatutCommande.BROUILLON) {
            throw new RegleGestionException("RG-C5", "Une commande n'est modifiable qu'en BROUILLON");
        }
        for (LigneQuantiteDto l : lignes) {
            LigneCommande ligne = c.ligne(l.ligneId());
            if (ligne == null) {
                throw new IntrouvableException("Ligne " + l.ligneId() + " introuvable dans la commande " + c.getNumero());
            }
            if (l.quantite() == null || l.quantite() <= 0) {
                throw new RegleGestionException("QUANTITE", "La quantité doit être positive");
            }
            ligne.setQuantite(l.quantite());
        }
        c.recalculerMontant();
        return versDto(c);
    }

    /** RG-C2 : montant <= seuil -> VALIDEE automatiquement ; sinon EN_ATTENTE_VALIDATION (directeur). */
    @Transactional
    public CommandeDto soumettre(Long id) {
        Commande c = trouver(id);
        BigDecimal seuil = workflow.parametreDecimal("seuil_validation");
        String nom = utilisateur.nom();
        if (c.getMontantTotal().compareTo(seuil) <= 0) {
            workflow.changerStatut(TYPE, c.getId(), c.getStatut().name(), StatutCommande.VALIDEE.name(), nom,
                    WorkflowService.ROLE_SYSTEME, "Montant inférieur ou égal au seuil : validation automatique");
            c.setStatut(StatutCommande.VALIDEE);
            c.marquerValidee(WorkflowService.ROLE_SYSTEME);
        } else {
            workflow.changerStatut(TYPE, c.getId(), c.getStatut().name(), StatutCommande.EN_ATTENTE_VALIDATION.name(),
                    nom, WorkflowService.ROLE_SYSTEME, "Montant supérieur au seuil : validation du directeur");
            c.setStatut(StatutCommande.EN_ATTENTE_VALIDATION);
        }
        return versDto(c);
    }

    /** RG-C3 : le créateur ne peut pas valider sa propre commande. */
    @Transactional
    public CommandeDto valider(Long id) {
        Commande c = trouver(id);
        String nom = utilisateur.nom();
        if (nom.equals(c.getCreePar())) {
            throw new RegleGestionException("RG-C3", "Le créateur d'une commande ne peut pas la valider");
        }
        workflow.changerStatut(TYPE, c.getId(), c.getStatut().name(), StatutCommande.VALIDEE.name(), nom, utilisateur.role());
        c.setStatut(StatutCommande.VALIDEE);
        c.marquerValidee(nom);
        return versDto(c);
    }

    /** Refus du directeur : retour en BROUILLON avec motif obligatoire. */
    @Transactional
    public CommandeDto refuser(Long id, String motif) {
        if (motif == null || motif.isBlank()) {
            throw new RegleGestionException("MOTIF", "Le motif du refus est obligatoire");
        }
        Commande c = trouver(id);
        workflow.changerStatut(TYPE, c.getId(), c.getStatut().name(), StatutCommande.BROUILLON.name(),
                utilisateur.nom(), utilisateur.role(), motif);
        c.setStatut(StatutCommande.BROUILLON);
        c.marquerRefusee(motif);
        return versDto(c);
    }

    @Transactional
    public CommandeDto annuler(Long id) {
        Commande c = trouver(id);
        workflow.changerStatut(TYPE, c.getId(), c.getStatut().name(), StatutCommande.ANNULEE.name(),
                utilisateur.nom(), utilisateur.role());
        c.setStatut(StatutCommande.ANNULEE);
        return versDto(c);
    }

    @Transactional
    public CommandeDto envoyer(Long id) {
        Commande c = trouver(id);
        workflow.changerStatut(TYPE, c.getId(), c.getStatut().name(), StatutCommande.ENVOYEE.name(),
                utilisateur.nom(), utilisateur.role());
        c.setStatut(StatutCommande.ENVOYEE);
        c.marquerEnvoyee();
        return versDto(c);
    }

    @Transactional(readOnly = true)
    public List<CommandeDto> lister(StatutCommande statut) {
        List<Commande> liste = statut == null ? commandes.findAllByOrderByIdDesc()
                : commandes.findByStatutOrderByIdDesc(statut);
        return liste.stream().map(this::versDto).toList();
    }

    @Transactional(readOnly = true)
    public CommandeDto detail(Long id) {
        return versDto(trouver(id));
    }

    // ---------- Contrat pour D (section 4.6) ----------

    /** RG-D4 : une facture ne se rattache qu'à une commande RECUE. */
    @Transactional(readOnly = true)
    public Commande getCommandeReceptionnee(Long commandeId) {
        Commande c = trouver(commandeId);
        if (c.getStatut() != StatutCommande.RECUE) {
            throw new RegleGestionException("RG-D4", "La commande " + c.getNumero() + " n'est pas réceptionnée");
        }
        return c;
    }

    /** Appelée par D quand la facture est VALIDEE : RECUE -> FACTUREE. */
    @Transactional
    public void marquerFacturee(Long commandeId, String utilisateurNom) {
        Commande c = trouver(commandeId);
        workflow.changerStatut(TYPE, c.getId(), c.getStatut().name(), StatutCommande.FACTUREE.name(),
                utilisateurNom, "COMPTABLE");
        c.setStatut(StatutCommande.FACTUREE);
    }

    /** Appelée par D quand tout est payé : FACTUREE -> PAYEE -> CLOTUREE. */
    @Transactional
    public void marquerPayee(Long commandeId, String utilisateurNom) {
        Commande c = trouver(commandeId);
        workflow.changerStatut(TYPE, c.getId(), c.getStatut().name(), StatutCommande.PAYEE.name(),
                utilisateurNom, "COMPTABLE");
        workflow.changerStatut(TYPE, c.getId(), StatutCommande.PAYEE.name(), StatutCommande.CLOTUREE.name(),
                utilisateurNom, WorkflowService.ROLE_SYSTEME);
        c.setStatut(StatutCommande.CLOTUREE);
    }

    /** Commandes RECUE, prêtes à être facturées (écran de D). */
    @Transactional(readOnly = true)
    public List<Commande> commandesAFacturer() {
        return commandes.findByStatutOrderByIdDesc(StatutCommande.RECUE);
    }

    // ---------- outils ----------

    private Commande trouver(Long id) {
        return commandes.findById(id)
                .orElseThrow(() -> new IntrouvableException("Commande " + id + " introuvable"));
    }

    private CommandeDto versDto(Commande c) {
        return CommandeDto.de(c, this::libelleProduit);
    }

    private String libelleProduit(Long produitId) {
        Produit p = stock.produit(produitId);
        return p == null ? null : p.getLibelle();
    }
}
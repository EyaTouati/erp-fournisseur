package tn.erp.fournisseur.c.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.c.dto.LigneReceptionCreationDto;
import tn.erp.fournisseur.c.dto.ReceptionCreationDto;
import tn.erp.fournisseur.c.dto.ReceptionDto;
import tn.erp.fournisseur.c.entity.*;
import tn.erp.fournisseur.c.repository.CommandeRepository;
import tn.erp.fournisseur.c.repository.ReceptionRepository;
import tn.erp.fournisseur.c.repository.ReclamationRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.integration.StockService;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

import java.time.Year;
import java.util.List;

/**
 * Sous-processus C : réception de la marchandise (RG-C4), contrôle qualité,
 * mise à jour du stock et réclamation si non conforme.
 */
@Service
public class ReceptionService {

    private final CommandeRepository commandes;
    private final ReceptionRepository receptions;
    private final ReclamationRepository reclamations;
    private final StockService stock;
    private final WorkflowService workflow;
    private final UtilisateurCourant utilisateur;

    public ReceptionService(CommandeRepository commandes, ReceptionRepository receptions,
                            ReclamationRepository reclamations, StockService stock,
                            WorkflowService workflow, UtilisateurCourant utilisateur) {
        this.commandes = commandes;
        this.receptions = receptions;
        this.reclamations = reclamations;
        this.stock = stock;
        this.workflow = workflow;
        this.utilisateur = utilisateur;
    }

    @Transactional
    public ReceptionDto receptionner(Long commandeId, ReceptionCreationDto dto) {
        Commande c = commandes.findById(commandeId)
                .orElseThrow(() -> new IntrouvableException("Commande " + commandeId + " introuvable"));

        // RG-C4 : seulement les commandes ENVOYEE ou PARTIELLEMENT_RECUE
        if (c.getStatut() != StatutCommande.ENVOYEE && c.getStatut() != StatutCommande.PARTIELLEMENT_RECUE) {
            throw new RegleGestionException("RG-C4",
                    "On ne réceptionne que des commandes ENVOYEE ou PARTIELLEMENT_RECUE");
        }
        if (dto.lignes() == null || dto.lignes().isEmpty()) {
            throw new RegleGestionException("QUANTITE", "Aucune ligne reçue");
        }

        String nom = utilisateur.nom();
        String role = utilisateur.role();
        String numero = String.format("REC-%d-%04d", Year.now().getValue(), receptions.count() + 1);
        Reception r = new Reception(numero, c, nom, dto.commentaire());

        for (LigneReceptionCreationDto l : dto.lignes()) {
            LigneCommande ligne = c.ligne(l.ligneCommandeId());
            if (ligne == null) {
                throw new IntrouvableException("Ligne " + l.ligneCommandeId() + " introuvable dans la commande");
            }
            int quantite = l.quantiteRecue() == null ? 0 : l.quantiteRecue();
            if (quantite < 0) {
                throw new RegleGestionException("QUANTITE", "La quantité reçue ne peut pas être négative");
            }
            // Blocage : on ne peut pas recevoir plus que ce qui reste à recevoir
            if (quantite > ligne.resteARecevoir()) {
                throw new RegleGestionException("RG-C4",
                        "Quantité reçue (" + quantite + ") supérieure au reste à recevoir ("
                                + ligne.resteARecevoir() + ")");
            }
            r.ajouterLigne(ligne, quantite, l.conforme() == null || l.conforme());
        }

        r = receptions.save(r);
        workflow.enregistrerCreation("RECEPTION", r.getId(), StatutReception.EN_COURS.name(), nom);

        // Stock : seules les quantités conformes entrent en stock et comptent comme reçues
        for (LigneReception lr : r.getLignes()) {
            if (lr.getConforme() && lr.getQuantiteRecue() > 0) {
                lr.getLigneCommande().ajouterRecu(lr.getQuantiteRecue());
                stock.entreeStock(lr.getLigneCommande().getProduitId(), lr.getQuantiteRecue(), r.getId());
            }
        }

        // Contrôle qualité
        if (r.toutEstConforme()) {
            workflow.changerStatut("RECEPTION", r.getId(), StatutReception.EN_COURS.name(),
                    StatutReception.CONFORME.name(), nom, role);
            r.setStatut(StatutReception.CONFORME);
        } else {
            String motif = (dto.commentaire() == null || dto.commentaire().isBlank())
                    ? "Marchandise non conforme (réception " + numero + ")" : dto.commentaire();
            workflow.changerStatut("RECEPTION", r.getId(), StatutReception.EN_COURS.name(),
                    StatutReception.NON_CONFORME.name(), nom, role, motif);
            r.setStatut(StatutReception.NON_CONFORME);
            Reclamation rec = reclamations.save(new Reclamation(r, motif));
            workflow.enregistrerCreation("RECLAMATION", rec.getId(), StatutReclamation.OUVERTE.name(), nom);
        }

        // Statut de la commande : RECUE si tout est reçu, sinon PARTIELLEMENT_RECUE
        StatutCommande nouveau = c.getStatut();
        if (c.estEntierementRecue()) {
            nouveau = StatutCommande.RECUE;
        } else if (c.aDejaRecu()) {
            nouveau = StatutCommande.PARTIELLEMENT_RECUE;
        }
        if (nouveau != c.getStatut()) {
            workflow.changerStatut("COMMANDE", c.getId(), c.getStatut().name(), nouveau.name(), nom, role);
            c.setStatut(nouveau);
        }
        return ReceptionDto.de(r);
    }

    @Transactional(readOnly = true)
    public List<ReceptionDto> lister(Long commandeId) {
        List<Reception> liste = commandeId == null ? receptions.findAllByOrderByIdDesc()
                : receptions.findByCommandeIdOrderByIdAsc(commandeId);
        return liste.stream().map(ReceptionDto::de).toList();
    }
}
package tn.erp.fournisseur.b.service;

import java.time.Year;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.b.dto.DemandeAchatCreationDto;
import tn.erp.fournisseur.b.dto.DemandeAchatDto;
import tn.erp.fournisseur.b.entity.DemandeAchat;
import tn.erp.fournisseur.b.entity.StatutDemandeAchat;
import tn.erp.fournisseur.b.repository.DemandeAchatRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.integration.Produit;
import tn.erp.fournisseur.commun.integration.ProduitRepository;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;
import tn.erp.fournisseur.commun.workflow.WorkflowService;

@Service
public class DemandeAchatService {

    private static final String TYPE = "DEMANDE_ACHAT";

    private final DemandeAchatRepository demandes;
    private final ProduitRepository produits;
    private final WorkflowService workflow;
    private final UtilisateurCourant utilisateur;

    public DemandeAchatService(DemandeAchatRepository demandes, ProduitRepository produits,
                               WorkflowService workflow, UtilisateurCourant utilisateur) {
        this.demandes = demandes;
        this.produits = produits;
        this.workflow = workflow;
        this.utilisateur = utilisateur;
    }

    @Transactional(readOnly = true)
    public List<DemandeAchatDto> lister(StatutDemandeAchat statut) {
        List<DemandeAchat> liste = (statut == null) ? demandes.findAll() : demandes.findByStatut(statut);
        return liste.stream().map(this::versDto).toList();
    }

    @Transactional(readOnly = true)
    public DemandeAchatDto obtenir(Long id) {
        return versDto(trouver(id));
    }

    @Transactional
    public DemandeAchatDto creer(DemandeAchatCreationDto dto) {
        produits.findById(dto.produitId())
                .orElseThrow(() -> new IntrouvableException("Produit " + dto.produitId() + " introuvable"));
        String numero = String.format("DA-%d-%04d", Year.now().getValue(), demandes.count() + 1);
        DemandeAchat d = demandes.save(new DemandeAchat(numero, dto.produitId(), dto.quantite(),
                dto.dateSouhaitee(), utilisateur.nom()));
        workflow.enregistrerCreation(TYPE, d.getId(), "BROUILLON", utilisateur.nom());
        return versDto(d);
    }

    @Transactional
    public DemandeAchatDto soumettre(Long id) {
        DemandeAchat d = trouver(id);
        changer(d, StatutDemandeAchat.SOUMISE, null);
        return versDto(d);
    }

    @Transactional
    public DemandeAchatDto accepter(Long id) {
        DemandeAchat d = trouver(id);
        changer(d, StatutDemandeAchat.ACCEPTEE, null);
        return versDto(d);
    }

    @Transactional
    public DemandeAchatDto refuser(Long id, String motif) {
        if (motif == null || motif.isBlank())
            throw new RegleGestionException("B-MOTIF", "Le motif de refus est obligatoire");
        DemandeAchat d = trouver(id);
        changer(d, StatutDemandeAchat.REFUSEE, motif);
        d.setMotifRefus(motif);
        return versDto(d);
    }

    private void changer(DemandeAchat d, StatutDemandeAchat nouveau, String commentaire) {
        if (commentaire == null) {
            workflow.changerStatut(TYPE, d.getId(), d.getStatut().name(), nouveau.name(),
                    utilisateur.nom(), utilisateur.role());
        } else {
            workflow.changerStatut(TYPE, d.getId(), d.getStatut().name(), nouveau.name(),
                    utilisateur.nom(), utilisateur.role(), commentaire);
        }
        d.setStatut(nouveau);
    }

    private DemandeAchat trouver(Long id) {
        return demandes.findById(id)
                .orElseThrow(() -> new IntrouvableException("Demande d'achat " + id + " introuvable"));
    }

    private DemandeAchatDto versDto(DemandeAchat d) {
        String libelle = produits.findById(d.getProduitId()).map(Produit::getLibelle).orElse(null);
        return DemandeAchatDto.de(d, libelle);
    }
}
package tn.erp.fournisseur.commun.workflow;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Trace d'un changement de statut (table historique_statut, script V1) :
 * qui, quand, de quel statut vers quel statut.
 */
@Entity
@Table(name = "historique_statut")
public class HistoriqueStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_document", nullable = false, length = 30)
    private String typeDocument;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    /** null pour la création du document. */
    @Column(name = "ancien_statut", length = 30)
    private String ancienStatut;

    @Column(name = "nouveau_statut", nullable = false, length = 30)
    private String nouveauStatut;

    @Column(name = "utilisateur", nullable = false, length = 50)
    private String utilisateur;

    @Column(name = "date_changement", nullable = false)
    private LocalDateTime dateChangement;

    /** Motif éventuel (refus, désactivation, litige...). */
    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    protected HistoriqueStatut() {
    }

    public HistoriqueStatut(String typeDocument, Long documentId, String ancienStatut, String nouveauStatut,
                            String utilisateur, String commentaire) {
        this.typeDocument = typeDocument;
        this.documentId = documentId;
        this.ancienStatut = ancienStatut;
        this.nouveauStatut = nouveauStatut;
        this.utilisateur = utilisateur;
        this.commentaire = commentaire;
        this.dateChangement = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTypeDocument() { return typeDocument; }
    public Long getDocumentId() { return documentId; }
    public String getAncienStatut() { return ancienStatut; }
    public String getNouveauStatut() { return nouveauStatut; }
    public String getUtilisateur() { return utilisateur; }
    public LocalDateTime getDateChangement() { return dateChangement; }
    public String getCommentaire() { return commentaire; }
}

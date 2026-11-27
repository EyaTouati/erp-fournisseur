package tn.erp.fournisseur.commun.workflow;

import jakarta.persistence.*;

/**
 * Un changement de statut autorisé (table transition_statut, script V1).
 * Exemple : COMMANDE, EN_ATTENTE_VALIDATION -> VALIDEE, rôle DIRECTEUR.
 */
@Entity
@Table(name = "transition_statut")
public class TransitionStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_document", nullable = false, length = 30)
    private String typeDocument;

    @Column(name = "statut_depart", nullable = false, length = 30)
    private String statutDepart;

    @Column(name = "statut_arrivee", nullable = false, length = 30)
    private String statutArrivee;

    /** MAGASINIER, ACHATS, DIRECTEUR, COMPTABLE ou SYSTEME. */
    @Column(name = "role_autorise", nullable = false, length = 30)
    private String roleAutorise;

    protected TransitionStatut() {
    }

    public TransitionStatut(String typeDocument, String statutDepart, String statutArrivee, String roleAutorise) {
        this.typeDocument = typeDocument;
        this.statutDepart = statutDepart;
        this.statutArrivee = statutArrivee;
        this.roleAutorise = roleAutorise;
    }

    public Long getId() { return id; }
    public String getTypeDocument() { return typeDocument; }
    public String getStatutDepart() { return statutDepart; }
    public String getStatutArrivee() { return statutArrivee; }
    public String getRoleAutorise() { return roleAutorise; }
}

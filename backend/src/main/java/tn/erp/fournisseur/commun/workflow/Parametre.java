package tn.erp.fournisseur.commun.workflow;

import jakarta.persistence.*;

/**
 * Paramètre modifiable sans toucher au code (table parametre, script V1).
 * Exemple : cle = seuil_validation, valeur = 5000.
 */
@Entity
@Table(name = "parametre")
public class Parametre {

    @Id
    @Column(name = "cle", length = 50)
    private String cle;

    @Column(name = "valeur", nullable = false, length = 100)
    private String valeur;

    @Column(name = "description")
    private String description;

    protected Parametre() {
    }

    public Parametre(String cle, String valeur, String description) {
        this.cle = cle;
        this.valeur = valeur;
        this.description = description;
    }

    public String getCle() { return cle; }
    public String getValeur() { return valeur; }
    public String getDescription() { return description; }
}

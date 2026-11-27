package tn.erp.fournisseur.c.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

/** Ligne d'un bon de commande (table ligne_commande, V4). */
@Entity
@Table(name = "ligne_commande")
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Column(name = "produit_id", nullable = false)
    private Long produitId;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", nullable = false, precision = 12, scale = 3)
    private BigDecimal prixUnitaire;

    /** Quantité reçue CONFORME, cumulée sur toutes les réceptions. */
    @Column(name = "quantite_recue", nullable = false)
    private Integer quantiteRecue;

    protected LigneCommande() {
    }

    LigneCommande(Commande commande, Long produitId, int quantite, BigDecimal prixUnitaire) {
        this.commande = commande;
        this.produitId = produitId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.quantiteRecue = 0;
    }

    public BigDecimal montant() {
        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public int resteARecevoir() {
        return quantite - quantiteRecue;
    }

    public void ajouterRecu(int quantite) {
        this.quantiteRecue = this.quantiteRecue + quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Long getId() { return id; }
    public Commande getCommande() { return commande; }
    public Long getProduitId() { return produitId; }
    public Integer getQuantite() { return quantite; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public Integer getQuantiteRecue() { return quantiteRecue; }
}
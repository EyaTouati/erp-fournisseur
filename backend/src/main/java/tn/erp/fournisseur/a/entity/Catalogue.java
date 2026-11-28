package tn.erp.fournisseur.a.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "catalogue")
public class Catalogue {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    // le produit est dans commun/integration : simple colonne
    @Column(name = "produit_id", nullable = false)
    private Long produitId;

    @Column(name = "prix_unitaire", nullable = false, precision = 12, scale = 3)
    private BigDecimal prixUnitaire;

    @Column(name = "delai_jours", nullable = false)
    private Integer delaiJours;

    protected Catalogue() { }

    public Catalogue(Fournisseur fournisseur, Long produitId, BigDecimal prixUnitaire, Integer delaiJours) {
        this.fournisseur = fournisseur;
        this.produitId = produitId;
        this.prixUnitaire = prixUnitaire;
        this.delaiJours = delaiJours;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setDelaiJours(Integer delaiJours) { this.delaiJours = delaiJours; }

    public Long getId() { return id; }
    public Fournisseur getFournisseur() { return fournisseur; }
    public Long getProduitId() { return produitId; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public Integer getDelaiJours() { return delaiJours; }
}

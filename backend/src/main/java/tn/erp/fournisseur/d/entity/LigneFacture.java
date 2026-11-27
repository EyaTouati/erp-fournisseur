package tn.erp.fournisseur.d.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ligne_facture")
public class LigneFacture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facture_id", nullable = false)
    private Long factureId;

    @Column(name = "produit_id", nullable = false)
    private Long produitId;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", nullable = false, precision = 12, scale = 3)
    private BigDecimal prixUnitaire;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFactureId() { return factureId; }
    public void setFactureId(Long factureId) { this.factureId = factureId; }
    public Long getProduitId() { return produitId; }
    public void setProduitId(Long produitId) { this.produitId = produitId; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
}

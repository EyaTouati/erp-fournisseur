package tn.erp.fournisseur.a.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CatalogueDTO {

    private Long id;

    @NotNull(message = "Le fournisseur est obligatoire")
    private Long fournisseurId;

    @NotNull(message = "Le produit est obligatoire")
    private Long produitId;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private BigDecimal prixUnitaire;

    @NotNull(message = "Le délai est obligatoire")
    @Min(value = 0, message = "Le délai ne peut pas être négatif")
    private Integer delaiJours;

    public CatalogueDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(Long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getDelaiJours() {
        return delaiJours;
    }

    public void setDelaiJours(Integer delaiJours) {
        this.delaiJours = delaiJours;
    }
}
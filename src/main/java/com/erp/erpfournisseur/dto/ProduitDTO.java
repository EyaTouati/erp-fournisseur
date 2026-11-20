package com.erp.erpfournisseur.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProduitDTO {

    private Long id;

    @NotBlank(message = "La référence est obligatoire")
    @Size(max = 30, message = "La référence ne doit pas dépasser 30 caractères")
    private String reference;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 150, message = "Le libellé ne doit pas dépasser 150 caractères")
    private String libelle;

    @Size(max = 20)
    private String unite;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stockActuel;

    @Min(value = 0, message = "Le seuil de réapprovisionnement ne peut pas être négatif")
    private Integer seuilReappro;

    public ProduitDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Integer getStockActuel() {
        return stockActuel;
    }

    public void setStockActuel(Integer stockActuel) {
        this.stockActuel = stockActuel;
    }

    public Integer getSeuilReappro() {
        return seuilReappro;
    }

    public void setSeuilReappro(Integer seuilReappro) {
        this.seuilReappro = seuilReappro;
    }
}
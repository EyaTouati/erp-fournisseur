
package com.erp.erpfournisseur.catalogue;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String reference;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String libelle;

    @Column(length = 20)
    private String unite = "unité";

    @Min(0)
    @Column(name = "stock_actuel", nullable = false)
    private int stockActuel = 0;

    @Min(0)
    @Column(name = "seuil_reappro", nullable = false)
    private int seuilReappro = 0;

    public Produit() {
    }

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

    public int getStockActuel() {
        return stockActuel;
    }

    public void setStockActuel(int stockActuel) {
        this.stockActuel = stockActuel;
    }

    public int getSeuilReappro() {
        return seuilReappro;
    }

    public void setSeuilReappro(int seuilReappro) {
        this.seuilReappro = seuilReappro;
    }
}

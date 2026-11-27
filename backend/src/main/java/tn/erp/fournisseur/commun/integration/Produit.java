package tn.erp.fournisseur.commun.integration;

import jakarta.persistence.*;

/**
 * Produit du module Stock (simulé) - table produit de la V1.
 * Partagé par A (catalogue), B (demandes d'achat), C (réception) et D (factures).
 */
@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true, length = 30)
    private String reference;

    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;

    @Column(name = "unite", length = 20)
    private String unite;

    @Column(name = "stock_actuel", nullable = false)
    private Integer stockActuel;

    @Column(name = "seuil_reappro", nullable = false)
    private Integer seuilReappro;

    protected Produit() {
    }

    public Produit(String reference, String libelle, int stockActuel, int seuilReappro) {
        this.reference = reference;
        this.libelle = libelle;
        this.unite = "unité";
        this.stockActuel = stockActuel;
        this.seuilReappro = seuilReappro;
    }

    /** Entrée en stock : appelée uniquement par StockService. */
    void ajouterAuStock(int quantite) {
        this.stockActuel = this.stockActuel + quantite;
    }

    /** Stock bas : stock_actuel <= seuil_reappro. */
    public boolean estEnAlerte() {
        return stockActuel <= seuilReappro;
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public String getLibelle() { return libelle; }
    public String getUnite() { return unite; }
    public Integer getStockActuel() { return stockActuel; }
    public Integer getSeuilReappro() { return seuilReappro; }
}
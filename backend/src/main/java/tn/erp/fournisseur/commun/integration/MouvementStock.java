package tn.erp.fournisseur.commun.integration;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Quantité envoyée au module Stock (simulé) - table mouvement_stock de la V1, écrite par C. */
@Entity
@Table(name = "mouvement_stock")
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "source", length = 30)
    private String source;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    protected MouvementStock() {
    }

    public MouvementStock(Produit produit, int quantite, String source, Long sourceId) {
        this.produit = produit;
        this.quantite = quantite;
        this.source = source;
        this.sourceId = sourceId;
        this.dateMouvement = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Produit getProduit() { return produit; }
    public Integer getQuantite() { return quantite; }
    public String getSource() { return source; }
    public Long getSourceId() { return sourceId; }
    public LocalDateTime getDateMouvement() { return dateMouvement; }
}
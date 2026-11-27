package tn.erp.fournisseur.c.entity;

import jakarta.persistence.*;

/** Ligne d'une réception (table ligne_reception, V4). */
@Entity
@Table(name = "ligne_reception")
public class LigneReception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reception_id", nullable = false)
    private Reception reception;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ligne_commande_id", nullable = false)
    private LigneCommande ligneCommande;

    @Column(name = "quantite_recue", nullable = false)
    private Integer quantiteRecue;

    @Column(name = "conforme", nullable = false)
    private Boolean conforme;

    protected LigneReception() {
    }

    LigneReception(Reception reception, LigneCommande ligneCommande, int quantiteRecue, boolean conforme) {
        this.reception = reception;
        this.ligneCommande = ligneCommande;
        this.quantiteRecue = quantiteRecue;
        this.conforme = conforme;
    }

    public Long getId() { return id; }
    public Reception getReception() { return reception; }
    public LigneCommande getLigneCommande() { return ligneCommande; }
    public Integer getQuantiteRecue() { return quantiteRecue; }
    public Boolean getConforme() { return conforme; }
}
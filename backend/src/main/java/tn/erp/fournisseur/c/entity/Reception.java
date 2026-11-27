package tn.erp.fournisseur.c.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Réception d'une livraison (table reception, V4). */
@Entity
@Table(name = "reception")
public class Reception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 20)
    private String numero;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 30)
    private StatutReception statut;

    @Column(name = "receptionne_par", nullable = false, length = 50)
    private String receptionnePar;

    @Column(name = "date_reception")
    private LocalDateTime dateReception;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    @OneToMany(mappedBy = "reception", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<LigneReception> lignes = new ArrayList<>();

    protected Reception() {
    }

    public Reception(String numero, Commande commande, String receptionnePar, String commentaire) {
        this.numero = numero;
        this.commande = commande;
        this.receptionnePar = receptionnePar;
        this.commentaire = commentaire;
        this.statut = StatutReception.EN_COURS;
        this.dateReception = LocalDateTime.now();
    }

    public LigneReception ajouterLigne(LigneCommande ligneCommande, int quantiteRecue, boolean conforme) {
        LigneReception ligne = new LigneReception(this, ligneCommande, quantiteRecue, conforme);
        lignes.add(ligne);
        return ligne;
    }

    public boolean toutEstConforme() {
        return lignes.stream().allMatch(LigneReception::getConforme);
    }

    public void setStatut(StatutReception statut) {
        this.statut = statut;
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public Commande getCommande() { return commande; }
    public StatutReception getStatut() { return statut; }
    public String getReceptionnePar() { return receptionnePar; }
    public LocalDateTime getDateReception() { return dateReception; }
    public String getCommentaire() { return commentaire; }
    public List<LigneReception> getLignes() { return lignes; }
}
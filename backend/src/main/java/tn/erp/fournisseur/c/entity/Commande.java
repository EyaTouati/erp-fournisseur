package tn.erp.fournisseur.c.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Bon de commande (table commande, V4). Créé uniquement depuis une offre RETENUE (RG-C1). */
@Entity
@Table(name = "commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 20)
    private String numero;

    @Column(name = "offre_id", nullable = false, unique = true)
    private Long offreId;

    @Column(name = "fournisseur_id", nullable = false)
    private Long fournisseurId;

    @Column(name = "montant_total", nullable = false, precision = 12, scale = 3)
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 30)
    private StatutCommande statut;

    @Column(name = "cree_par", nullable = false, length = 50)
    private String creePar;

    @Column(name = "valide_par", length = 50)
    private String validePar;

    @Column(name = "motif_refus", length = 255)
    private String motifRefus;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<LigneCommande> lignes = new ArrayList<>();

    protected Commande() {
    }

    public Commande(String numero, Long offreId, Long fournisseurId, String creePar) {
        this.numero = numero;
        this.offreId = offreId;
        this.fournisseurId = fournisseurId;
        this.creePar = creePar;
        this.statut = StatutCommande.BROUILLON;
        this.montantTotal = BigDecimal.ZERO;
        this.dateCreation = LocalDateTime.now();
    }

    public LigneCommande ajouterLigne(Long produitId, int quantite, BigDecimal prixUnitaire) {
        LigneCommande ligne = new LigneCommande(this, produitId, quantite, prixUnitaire);
        lignes.add(ligne);
        recalculerMontant();
        return ligne;
    }

    public void recalculerMontant() {
        this.montantTotal = lignes.stream().map(LigneCommande::montant).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Toutes les lignes ont été reçues conformes en totalité. */
    public boolean estEntierementRecue() {
        return !lignes.isEmpty() && lignes.stream().allMatch(l -> l.resteARecevoir() == 0);
    }

    /** Au moins une quantité conforme a déjà été reçue. */
    public boolean aDejaRecu() {
        return lignes.stream().anyMatch(l -> l.getQuantiteRecue() > 0);
    }

    public LigneCommande ligne(Long ligneId) {
        return lignes.stream().filter(l -> ligneId != null && ligneId.equals(l.getId())).findFirst().orElse(null);
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public void marquerValidee(String validePar) {
        this.validePar = validePar;
        this.dateValidation = LocalDateTime.now();
    }

    public void marquerRefusee(String motif) {
        this.motifRefus = motif;
    }

    public void marquerEnvoyee() {
        this.dateEnvoi = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public Long getOffreId() { return offreId; }
    public Long getFournisseurId() { return fournisseurId; }
    public BigDecimal getMontantTotal() { return montantTotal; }
    public StatutCommande getStatut() { return statut; }
    public String getCreePar() { return creePar; }
    public String getValidePar() { return validePar; }
    public String getMotifRefus() { return motifRefus; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateValidation() { return dateValidation; }
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public List<LigneCommande> getLignes() { return lignes; }
}
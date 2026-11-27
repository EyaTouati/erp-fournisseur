package tn.erp.fournisseur.b.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "offre")
public class Offre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "demande_prix_id")
    private DemandePrix demandePrix;

    @Column(name = "fournisseur_id", nullable = false)
    private Long fournisseurId;

    @Column(name = "prix_unitaire", nullable = false, precision = 12, scale = 3)
    private BigDecimal prixUnitaire;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "delai_jours", nullable = false)
    private Integer delaiJours;

    @Column(length = 255)
    private String conditions;

    @Column(name = "date_validite", nullable = false)
    private LocalDate dateValidite;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private StatutOffre statut;

    @Column(columnDefinition = "TEXT")
    private String justification;

    @Column(name = "date_reception")
    private LocalDateTime dateReception;

    protected Offre() { }

    public Offre(DemandePrix demandePrix, Long fournisseurId, BigDecimal prixUnitaire,
                 Integer quantite, Integer delaiJours, String conditions, LocalDate dateValidite) {
        this.demandePrix = demandePrix;
        this.fournisseurId = fournisseurId;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
        this.delaiJours = delaiJours;
        this.conditions = conditions;
        this.dateValidite = dateValidite;
        this.statut = StatutOffre.RECUE;
        this.dateReception = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public DemandePrix getDemandePrix() { return demandePrix; }
    public Long getFournisseurId() { return fournisseurId; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public Integer getQuantite() { return quantite; }
    public Integer getDelaiJours() { return delaiJours; }
    public String getConditions() { return conditions; }
    public LocalDate getDateValidite() { return dateValidite; }
    public StatutOffre getStatut() { return statut; }
    public String getJustification() { return justification; }
    public LocalDateTime getDateReception() { return dateReception; }

    public void setStatut(StatutOffre statut) { this.statut = statut; }
    public void setJustification(String justification) { this.justification = justification; }
}
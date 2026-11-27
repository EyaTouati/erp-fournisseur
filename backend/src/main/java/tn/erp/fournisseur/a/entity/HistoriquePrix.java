package tn.erp.fournisseur.a.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_prix")
public class HistoriquePrix {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "catalogue_id")
    private Catalogue catalogue;

    @Column(name = "ancien_prix", nullable = false, precision = 12, scale = 3)
    private BigDecimal ancienPrix;

    @Column(name = "nouveau_prix", nullable = false, precision = 12, scale = 3)
    private BigDecimal nouveauPrix;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "modifie_par", length = 50)
    private String modifiePar;

    protected HistoriquePrix() { }

    public HistoriquePrix(Catalogue catalogue, BigDecimal ancienPrix, BigDecimal nouveauPrix, String modifiePar) {
        this.catalogue = catalogue;
        this.ancienPrix = ancienPrix;
        this.nouveauPrix = nouveauPrix;
        this.modifiePar = modifiePar;
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Catalogue getCatalogue() { return catalogue; }
    public BigDecimal getAncienPrix() { return ancienPrix; }
    public BigDecimal getNouveauPrix() { return nouveauPrix; }
    public LocalDateTime getDateModification() { return dateModification; }
    public String getModifiePar() { return modifiePar; }
}

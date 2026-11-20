package com.erp.erpfournisseur.historique;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_prix")
public class HistoriquePrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "catalogue_id", nullable = false)
    private com.erp.erpfournisseur.catalogue.Catalogue catalogue;

    @Column(name = "ancien_prix", nullable = false, precision = 12, scale = 3)
    private BigDecimal ancienPrix;

    @Column(name = "nouveau_prix", nullable = false, precision = 12, scale = 3)
    private BigDecimal nouveauPrix;

    @Column(name = "date_modification", nullable = false)
    private LocalDateTime dateModification = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public com.erp.erpfournisseur.catalogue.Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(com.erp.erpfournisseur.catalogue.Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    public BigDecimal getAncienPrix() {
        return ancienPrix;
    }

    public void setAncienPrix(BigDecimal ancienPrix) {
        this.ancienPrix = ancienPrix;
    }

    public BigDecimal getNouveauPrix() {
        return nouveauPrix;
    }

    public void setNouveauPrix(BigDecimal nouveauPrix) {
        this.nouveauPrix = nouveauPrix;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
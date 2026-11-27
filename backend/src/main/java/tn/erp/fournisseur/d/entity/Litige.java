package tn.erp.fournisseur.d.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "litige")
public class Litige {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facture_id", nullable = false)
    private Long factureId;

    @Column(nullable = false)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutLitige statut = StatutLitige.OUVERT;

    @Column(name = "date_ouverture")
    private LocalDateTime dateOuverture = LocalDateTime.now();

    @Column(name = "date_resolution")
    private LocalDateTime dateResolution;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFactureId() { return factureId; }
    public void setFactureId(Long factureId) { this.factureId = factureId; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public StatutLitige getStatut() { return statut; }
    public void setStatut(StatutLitige statut) { this.statut = statut; }
    public LocalDateTime getDateOuverture() { return dateOuverture; }
    public void setDateOuverture(LocalDateTime dateOuverture) { this.dateOuverture = dateOuverture; }
    public LocalDateTime getDateResolution() { return dateResolution; }
    public void setDateResolution(LocalDateTime dateResolution) { this.dateResolution = dateResolution; }
}
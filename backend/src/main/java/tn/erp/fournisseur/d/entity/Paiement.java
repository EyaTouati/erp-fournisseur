package tn.erp.fournisseur.d.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiement")
public class Paiement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facture_id", nullable = false)
    private Long factureId;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal montant;

    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement;

    @Column(length = 20)
    private String mode;

    @Column(length = 50)
    private String reference;

    @Column(name = "paye_par", nullable = false, length = 50)
    private String payePar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFactureId() { return factureId; }
    public void setFactureId(Long factureId) { this.factureId = factureId; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public LocalDate getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getPayePar() { return payePar; }
    public void setPayePar(String payePar) { this.payePar = payePar; }
}
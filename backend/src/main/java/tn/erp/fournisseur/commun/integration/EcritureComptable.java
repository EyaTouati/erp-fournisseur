package tn.erp.fournisseur.commun.integration;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Écriture envoyée au module Comptabilité (simulé) - table ecriture_comptable de la V1, écrite par D. */
@Entity
@Table(name = "ecriture_comptable")
public class EcritureComptable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** FACTURE_VALIDEE ou PAIEMENT. */
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    /** id de la facture ou du paiement. */
    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "montant", nullable = false, precision = 12, scale = 3)
    private BigDecimal montant;

    @Column(name = "date_ecriture")
    private LocalDateTime dateEcriture;

    protected EcritureComptable() {
    }

    public EcritureComptable(String type, Long referenceId, BigDecimal montant) {
        this.type = type;
        this.referenceId = referenceId;
        this.montant = montant;
        this.dateEcriture = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public Long getReferenceId() { return referenceId; }
    public BigDecimal getMontant() { return montant; }
    public LocalDateTime getDateEcriture() { return dateEcriture; }
}
package tn.erp.fournisseur.c.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Réclamation au fournisseur après une réception non conforme (table reclamation, V4). */
@Entity
@Table(name = "reclamation")
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reception_id", nullable = false)
    private Reception reception;

    @Column(name = "motif", nullable = false, length = 255)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 30)
    private StatutReclamation statut;

    @Column(name = "date_reclamation")
    private LocalDateTime dateReclamation;

    protected Reclamation() {
    }

    public Reclamation(Reception reception, String motif) {
        this.reception = reception;
        this.motif = motif.length() > 255 ? motif.substring(0, 255) : motif;
        this.statut = StatutReclamation.OUVERTE;
        this.dateReclamation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Reception getReception() { return reception; }
    public String getMotif() { return motif; }
    public StatutReclamation getStatut() { return statut; }
    public LocalDateTime getDateReclamation() { return dateReclamation; }
}
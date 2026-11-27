package tn.erp.fournisseur.b.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande_achat")
public class DemandeAchat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @Column(name = "produit_id", nullable = false)
    private Long produitId;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "date_souhaitee")
    private LocalDate dateSouhaitee;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private StatutDemandeAchat statut;

    @Column(name = "motif_refus", length = 255)
    private String motifRefus;

    @Column(name = "cree_par", nullable = false, length = 50)
    private String creePar;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    protected DemandeAchat() { }

    public DemandeAchat(String numero, Long produitId, Integer quantite,
                        LocalDate dateSouhaitee, String creePar) {
        this.numero = numero;
        this.produitId = produitId;
        this.quantite = quantite;
        this.dateSouhaitee = dateSouhaitee;
        this.creePar = creePar;
        this.statut = StatutDemandeAchat.BROUILLON;
        this.dateCreation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public Long getProduitId() { return produitId; }
    public Integer getQuantite() { return quantite; }
    public LocalDate getDateSouhaitee() { return dateSouhaitee; }
    public StatutDemandeAchat getStatut() { return statut; }
    public String getMotifRefus() { return motifRefus; }
    public String getCreePar() { return creePar; }
    public LocalDateTime getDateCreation() { return dateCreation; }

    public void setStatut(StatutDemandeAchat statut) { this.statut = statut; }
    public void setMotifRefus(String motifRefus) { this.motifRefus = motifRefus; }
}
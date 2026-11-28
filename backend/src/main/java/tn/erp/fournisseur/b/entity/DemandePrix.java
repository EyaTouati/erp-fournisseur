package tn.erp.fournisseur.b.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "demande_prix")
public class DemandePrix {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @ManyToOne(optional = false)
    @JoinColumn(name = "demande_achat_id")
    private DemandeAchat demandeAchat;

    @Column(name = "date_limite", nullable = false)
    private LocalDate dateLimite;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private StatutDemandePrix statut;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    // table demande_prix_fournisseur : fournisseurs consultés
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "demande_prix_fournisseur",
            joinColumns = @JoinColumn(name = "demande_prix_id"))
    @Column(name = "fournisseur_id")
    private Set<Long> fournisseurIds = new HashSet<>();

    protected DemandePrix() { }

    public DemandePrix(String numero, DemandeAchat demandeAchat, LocalDate dateLimite,
                       Set<Long> fournisseurIds) {
        this.numero = numero;
        this.demandeAchat = demandeAchat;
        this.dateLimite = dateLimite;
        this.fournisseurIds = new HashSet<>(fournisseurIds);
        this.statut = StatutDemandePrix.BROUILLON;
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public DemandeAchat getDemandeAchat() { return demandeAchat; }
    public LocalDate getDateLimite() { return dateLimite; }
    public StatutDemandePrix getStatut() { return statut; }
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public Set<Long> getFournisseurIds() { return fournisseurIds; }

    public void setStatut(StatutDemandePrix statut) { this.statut = statut; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
}
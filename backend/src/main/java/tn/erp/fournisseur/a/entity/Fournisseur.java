package tn.erp.fournisseur.a.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fournisseur")
public class Fournisseur {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "nom", nullable = false, length = 150)
    private String nom;

    @Column(name = "identifiant_fiscal", nullable = false, length = 30)
    private String identifiantFiscal;

    @Column(name = "adresse", length = 255)
    private String adresse;

    @Column(name = "telephone", length = 30)
    private String telephone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "conditions_paiement", length = 100)
    private String conditionsPaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 30)
    private StatutFournisseur statut;

    @Column(name = "motif_desactivation", length = 255)
    private String motifDesactivation;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    protected Fournisseur() { }

    public Fournisseur(String code, String nom, String identifiantFiscal, String adresse,
                       String telephone, String email, String conditionsPaiement) {
        this.code = code;
        this.nom = nom;
        this.identifiantFiscal = identifiantFiscal;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.conditionsPaiement = conditionsPaiement;
        this.statut = StatutFournisseur.ACTIF;
        this.dateCreation = LocalDateTime.now();
    }

    public void modifier(String nom, String identifiantFiscal, String adresse, String telephone,
                         String email, String conditionsPaiement) {
        this.nom = nom;
        this.identifiantFiscal = identifiantFiscal;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.conditionsPaiement = conditionsPaiement;
    }

    public void desactiver(String motif) {
        this.statut = StatutFournisseur.INACTIF;
        this.motifDesactivation = motif;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getNom() { return nom; }
    public String getIdentifiantFiscal() { return identifiantFiscal; }
    public String getAdresse() { return adresse; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getConditionsPaiement() { return conditionsPaiement; }
    public StatutFournisseur getStatut() { return statut; }
    public String getMotifDesactivation() { return motifDesactivation; }
    public LocalDateTime getDateCreation() { return dateCreation; }
}

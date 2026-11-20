package com.erp.erpfournisseur.fournisseur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fournisseur")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du fournisseur est obligatoire")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères")
    @Column(nullable = false, length = 150)
    private String nom;
    @Size(max = 50, message = "L'identifiant fiscal ne doit pas dépasser 50 caractères")
    @Column(name = "identifiant_fiscal", unique = true, length = 50)
    private String identifiantFiscal;

    @Email(message = "L'adresse email est invalide")
    @Size(max = 150, message = "L'email ne doit pas dépasser 150 caractères")
    @Column(length = 150)
    private String email;

    @Size(max = 30, message = "Le téléphone ne doit pas dépasser 30 caractères")
    @Column(length = 30)
    private String telephone;

    private String adresse;

    private Boolean actif = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "motif_desactivation")
    private String motifDesactivation;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Fournisseur() {
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getIdentifiantFiscal() {
        return identifiantFiscal;
    }

    public void setIdentifiantFiscal(String identifiantFiscal) {
        this.identifiantFiscal = identifiantFiscal;
    }
    public String getMotifDesactivation() {
        return motifDesactivation;
    }

    public void setMotifDesactivation(String motifDesactivation) {
        this.motifDesactivation = motifDesactivation;
    }
}
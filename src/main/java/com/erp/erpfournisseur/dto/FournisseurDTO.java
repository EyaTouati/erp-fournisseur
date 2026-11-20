
package com.erp.erpfournisseur.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FournisseurDTO {

    private Long id;

    @NotBlank(message = "Le nom du fournisseur est obligatoire")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères")
    private String nom;
    @Size(max = 50, message = "L'identifiant fiscal ne doit pas dépasser 50 caractères")
    private String identifiantFiscal;

    @Email(message = "L'adresse email est invalide")
    @Size(max = 150, message = "L'email ne doit pas dépasser 150 caractères")
    private String email;

    @Size(max = 30, message = "Le téléphone ne doit pas dépasser 30 caractères")
    private String telephone;

    private String adresse;
    private Boolean actif;
    private String motifDesactivation;
    public String getMotifDesactivation() {
        return motifDesactivation;
    }

    public void setMotifDesactivation(String motifDesactivation) {
        this.motifDesactivation = motifDesactivation;
    }
    public FournisseurDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public String getIdentifiantFiscal() {
        return identifiantFiscal;
    }

    public void setIdentifiantFiscal(String identifiantFiscal) {
        this.identifiantFiscal = identifiantFiscal;
    }
}

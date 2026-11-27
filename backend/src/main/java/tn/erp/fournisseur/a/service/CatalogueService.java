package tn.erp.fournisseur.a.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.a.dto.*;
import tn.erp.fournisseur.a.entity.Catalogue;
import tn.erp.fournisseur.a.entity.Fournisseur;
import tn.erp.fournisseur.a.entity.HistoriquePrix;
import tn.erp.fournisseur.a.entity.StatutFournisseur;
import tn.erp.fournisseur.a.repository.CatalogueRepository;
import tn.erp.fournisseur.a.repository.FournisseurRepository;
import tn.erp.fournisseur.a.repository.HistoriquePrixRepository;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;
import tn.erp.fournisseur.commun.integration.Produit;
import tn.erp.fournisseur.commun.integration.ProduitRepository;
import tn.erp.fournisseur.commun.securite.UtilisateurCourant;

import java.util.List;

@Service
public class CatalogueService {

    private final CatalogueRepository catalogues;
    private final HistoriquePrixRepository historiques;
    private final FournisseurRepository fournisseurs;
    private final ProduitRepository produits;
    private final UtilisateurCourant utilisateur;

    public CatalogueService(CatalogueRepository catalogues, HistoriquePrixRepository historiques,
                            FournisseurRepository fournisseurs, ProduitRepository produits,
                            UtilisateurCourant utilisateur) {
        this.catalogues = catalogues;
        this.historiques = historiques;
        this.fournisseurs = fournisseurs;
        this.produits = produits;
        this.utilisateur = utilisateur;
    }

    @Transactional(readOnly = true)
    public List<CatalogueDto> lister(Long fournisseurId) {
        if (!fournisseurs.existsById(fournisseurId))
            throw new IntrouvableException("Fournisseur " + fournisseurId + " introuvable");
        return catalogues.findByFournisseurId(fournisseurId).stream()
                .map(c -> CatalogueDto.de(c, produits.findById(c.getProduitId()).orElse(null)))
                .toList();
    }

    @Transactional
    public CatalogueDto ajouter(Long fournisseurId, CatalogueCreationDto dto) {
        Fournisseur f = fournisseurs.findById(fournisseurId)
                .orElseThrow(() -> new IntrouvableException("Fournisseur " + fournisseurId + " introuvable"));
        if (f.getStatut() != StatutFournisseur.ACTIF)
            throw new RegleGestionException("RG-A3", "Un fournisseur inactif ne peut pas modifier son catalogue");
        if (catalogues.existsByFournisseurIdAndProduitId(fournisseurId, dto.produitId()))
            throw new RegleGestionException("RG-A7", "Ce produit est déjà dans le catalogue du fournisseur");
        Produit p = produits.findById(dto.produitId())
                .orElseThrow(() -> new IntrouvableException("Produit " + dto.produitId() + " introuvable"));
        Catalogue c = catalogues.save(new Catalogue(f, dto.produitId(), dto.prixUnitaire(), dto.delaiJours()));
        return CatalogueDto.de(c, p);
    }

    @Transactional
    public CatalogueDto modifierPrix(Long catalogueId, PrixDto dto) {
        Catalogue c = catalogues.findById(catalogueId)
                .orElseThrow(() -> new IntrouvableException("Ligne de catalogue " + catalogueId + " introuvable"));
        if (c.getPrixUnitaire().compareTo(dto.prixUnitaire()) != 0) {
            // l'ancien prix est conservé (seulement si le prix change)
            historiques.save(new HistoriquePrix(c, c.getPrixUnitaire(), dto.prixUnitaire(), utilisateur.nom()));
        }
        c.setPrixUnitaire(dto.prixUnitaire());
        c.setDelaiJours(dto.delaiJours());
        return CatalogueDto.de(c, produits.findById(c.getProduitId()).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<HistoriquePrixDto> historique(Long catalogueId) {
        if (!catalogues.existsById(catalogueId))
            throw new IntrouvableException("Ligne de catalogue " + catalogueId + " introuvable");
        return historiques.findByCatalogueIdOrderByDateModificationDesc(catalogueId).stream()
                .map(HistoriquePrixDto::de)
                .toList();
    }
}

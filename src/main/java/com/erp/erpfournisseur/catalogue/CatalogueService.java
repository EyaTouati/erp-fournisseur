
package com.erp.erpfournisseur.catalogue;

import com.erp.erpfournisseur.fournisseur.Fournisseur;
import com.erp.erpfournisseur.fournisseur.FournisseurRepository;
import com.erp.erpfournisseur.historique.HistoriquePrix;
import com.erp.erpfournisseur.historique.HistoriquePrixRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogueService {

    private final CatalogueRepository catalogueRepository;
    private final FournisseurRepository fournisseurRepository;
    private final ProduitRepository produitRepository;
    private final HistoriquePrixRepository historiquePrixRepository;

    public CatalogueService(
            CatalogueRepository catalogueRepository,
            FournisseurRepository fournisseurRepository,
            ProduitRepository produitRepository,
            HistoriquePrixRepository historiquePrixRepository) {

        this.catalogueRepository = catalogueRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.produitRepository = produitRepository;
        this.historiquePrixRepository = historiquePrixRepository;
    }

    public List<Catalogue> getAll() {
        return catalogueRepository.findAll();
    }

    public Catalogue getById(Long id) {
        return catalogueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catalogue introuvable"));
    }

    public Catalogue create(Catalogue catalogue, Long fournisseurId, Long produitId) {

        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        catalogue.setFournisseur(fournisseur);
        catalogue.setProduit(produit);

        return catalogueRepository.save(catalogue);
    }

    @Transactional
    public Catalogue update(Long id, Catalogue catalogue, Long fournisseurId, Long produitId) {

        Catalogue existing = getById(id);

        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        if (existing.getPrixUnitaire().compareTo(catalogue.getPrixUnitaire()) != 0) {

            HistoriquePrix historique = new HistoriquePrix();
            historique.setCatalogue(existing);
            historique.setAncienPrix(existing.getPrixUnitaire());
            historique.setNouveauPrix(catalogue.getPrixUnitaire());

            historiquePrixRepository.save(historique);
        }

        existing.setFournisseur(fournisseur);
        existing.setProduit(produit);
        existing.setPrixUnitaire(catalogue.getPrixUnitaire());
        existing.setDelaiJours(catalogue.getDelaiJours());

        return catalogueRepository.save(existing);
    }

    public void delete(Long id) {
        Catalogue catalogue = getById(id);
        catalogueRepository.delete(catalogue);
    }
}


package com.erp.erpfournisseur.catalogue;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produit not found"));
    }

    public Produit createProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public Produit updateProduit(Long id, Produit produit) {
        Produit existing = getProduitById(id);

        existing.setReference(produit.getReference());
        existing.setLibelle(produit.getLibelle());
        existing.setUnite(produit.getUnite());
        existing.setStockActuel(produit.getStockActuel());
        existing.setSeuilReappro(produit.getSeuilReappro());

        return produitRepository.save(existing);
    }

    public void deleteProduit(Long id) {
        Produit produit = getProduitById(id);
        produitRepository.delete(produit);
    }
}

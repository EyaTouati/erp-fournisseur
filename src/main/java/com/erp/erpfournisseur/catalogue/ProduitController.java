package com.erp.erpfournisseur.catalogue;

import com.erp.erpfournisseur.dto.ProduitDTO;
import com.erp.erpfournisseur.dto.ProduitMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public List<ProduitDTO> getAllProduits() {
        return produitService.getAllProduits()
                .stream()
                .map(ProduitMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ProduitDTO getProduitById(@PathVariable Long id) {
        return ProduitMapper.toDTO(produitService.getProduitById(id));
    }

    @PostMapping
    public ProduitDTO createProduit(@Valid @RequestBody ProduitDTO dto) {
        Produit produit = ProduitMapper.toEntity(dto);
        Produit saved = produitService.createProduit(produit);
        return ProduitMapper.toDTO(saved);
    }

    @PutMapping("/{id}")
    public ProduitDTO updateProduit(
            @PathVariable Long id,
            @Valid @RequestBody ProduitDTO dto) {

        Produit produit = ProduitMapper.toEntity(dto);
        Produit updated = produitService.updateProduit(id, produit);
        return ProduitMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
    }
}
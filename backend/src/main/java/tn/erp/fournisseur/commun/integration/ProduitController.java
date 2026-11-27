package tn.erp.fournisseur.commun.integration;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Section 12.2 du dossier de référence : produits et alertes de stock bas. */
@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final StockService stock;

    public ProduitController(StockService stock) {
        this.stock = stock;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<ProduitDto> tous() {
        return stock.tousLesProduits().stream().map(ProduitDto::de).toList();
    }

    @GetMapping("/alertes")
    @PreAuthorize("hasAnyRole('MAGASINIER', 'ACHATS')")
    public List<ProduitDto> alertes() {
        return stock.produitsEnAlerte().stream().map(ProduitDto::de).toList();
    }
}
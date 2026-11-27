package tn.erp.fournisseur.commun.integration;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.commun.erreurs.IntrouvableException;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;

import java.util.List;

/**
 * Simulation du module Stock (dossier de référence, section 11).
 * entreeStock est appelée par C après une réception conforme ; produitsEnAlerte sert à B.
 */
@Service
public class StockService {

    private final ProduitRepository produits;
    private final MouvementStockRepository mouvements;

    public StockService(ProduitRepository produits, MouvementStockRepository mouvements) {
        this.produits = produits;
        this.mouvements = mouvements;
    }

    /** Ajoute la quantité au stock du produit et trace le mouvement (source RECEPTION). */
    @Transactional
    public void entreeStock(Long produitId, int quantite, Long receptionId) {
        if (quantite <= 0) {
            throw new RegleGestionException("STOCK", "La quantité entrée en stock doit être positive");
        }
        Produit produit = produits.findById(produitId)
                .orElseThrow(() -> new IntrouvableException("Produit " + produitId + " introuvable"));
        produit.ajouterAuStock(quantite);
        mouvements.save(new MouvementStock(produit, quantite, "RECEPTION", receptionId));
    }

    /** Produits dont le stock est sous le seuil de réapprovisionnement. */
    @Transactional(readOnly = true)
    public List<Produit> produitsEnAlerte() {
        return produits.produitsEnAlerte();
    }

    @Transactional(readOnly = true)
    public List<Produit> tousLesProduits() {
        return produits.findAllByOrderByLibelleAsc();
    }

    @Transactional(readOnly = true)
    public Produit produit(Long produitId) {
        return produits.findById(produitId)
                .orElseThrow(() -> new IntrouvableException("Produit " + produitId + " introuvable"));
    }
}
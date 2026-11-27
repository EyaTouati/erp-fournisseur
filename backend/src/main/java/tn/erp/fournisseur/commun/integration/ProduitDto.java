package tn.erp.fournisseur.commun.integration;

public record ProduitDto(Long id, String reference, String libelle, String unite,
                         Integer stockActuel, Integer seuilReappro, boolean enAlerte) {

    public static ProduitDto de(Produit p) {
        return new ProduitDto(p.getId(), p.getReference(), p.getLibelle(), p.getUnite(),
                p.getStockActuel(), p.getSeuilReappro(), p.estEnAlerte());
    }
}
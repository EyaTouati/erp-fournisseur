package tn.erp.fournisseur.commun.integration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findAllByOrderByLibelleAsc();

    @Query("select p from Produit p where p.stockActuel <= p.seuilReappro order by p.libelle")
    List<Produit> produitsEnAlerte();
}
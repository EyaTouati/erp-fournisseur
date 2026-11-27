package tn.erp.fournisseur.a.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.a.entity.Catalogue;

import java.util.List;

public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {
    List<Catalogue> findByFournisseurId(Long fournisseurId);
    boolean existsByFournisseurIdAndProduitId(Long fournisseurId, Long produitId);
}

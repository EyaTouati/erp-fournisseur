package tn.erp.fournisseur.a.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.a.entity.HistoriquePrix;

import java.util.List;

public interface HistoriquePrixRepository extends JpaRepository<HistoriquePrix, Long> {
    List<HistoriquePrix> findByCatalogueIdOrderByDateModificationDesc(Long catalogueId);
}

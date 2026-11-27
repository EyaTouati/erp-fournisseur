package tn.erp.fournisseur.a.historique;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoriquePrixRepository
        extends JpaRepository<HistoriquePrix, Long> {

    List<HistoriquePrix> findByCatalogueId(Long catalogueId);
}
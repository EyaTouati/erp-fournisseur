package tn.erp.fournisseur.b.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.b.entity.DemandePrix;

public interface DemandePrixRepository extends JpaRepository<DemandePrix, Long> {
}
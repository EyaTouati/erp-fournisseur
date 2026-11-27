package tn.erp.fournisseur.d.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.d.entity.Facture;

public interface FactureRepository extends JpaRepository<Facture, Long> {}
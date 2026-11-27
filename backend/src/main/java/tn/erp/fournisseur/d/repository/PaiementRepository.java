package tn.erp.fournisseur.d.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.d.entity.Paiement;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {}

package tn.erp.fournisseur.d.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.d.entity.LigneFacture;

public interface LigneFactureRepository extends JpaRepository<LigneFacture, Long> {}
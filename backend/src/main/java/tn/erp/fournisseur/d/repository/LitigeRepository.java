package tn.erp.fournisseur.d.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.d.entity.Litige;

public interface LitigeRepository extends JpaRepository<Litige, Long> {}
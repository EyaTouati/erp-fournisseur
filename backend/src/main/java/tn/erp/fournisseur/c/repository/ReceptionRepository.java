package tn.erp.fournisseur.c.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.c.entity.Reception;

import java.util.List;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    List<Reception> findByCommandeIdOrderByIdAsc(Long commandeId);

    List<Reception> findAllByOrderByIdDesc();
}
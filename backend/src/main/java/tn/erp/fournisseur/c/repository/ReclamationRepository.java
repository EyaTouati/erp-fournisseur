package tn.erp.fournisseur.c.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.c.entity.Reclamation;
import tn.erp.fournisseur.c.entity.StatutReclamation;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    List<Reclamation> findByStatutOrderByIdDesc(StatutReclamation statut);

    List<Reclamation> findAllByOrderByIdDesc();
}
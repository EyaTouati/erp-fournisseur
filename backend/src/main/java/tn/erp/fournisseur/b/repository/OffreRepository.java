package tn.erp.fournisseur.b.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.b.entity.Offre;

public interface OffreRepository extends JpaRepository<Offre, Long> {
    List<Offre> findByDemandePrixId(Long demandePrixId);
    boolean existsByDemandePrixIdAndFournisseurId(Long demandePrixId, Long fournisseurId);
}
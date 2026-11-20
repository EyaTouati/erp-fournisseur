package tn.erp.fournisseur.commun.workflow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransitionStatutRepository extends JpaRepository<TransitionStatut, Long> {

    List<TransitionStatut> findByTypeDocumentAndStatutDepartAndStatutArrivee(
            String typeDocument, String statutDepart, String statutArrivee);
}

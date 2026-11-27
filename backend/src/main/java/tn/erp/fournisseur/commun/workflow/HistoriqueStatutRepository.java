package tn.erp.fournisseur.commun.workflow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {

    List<HistoriqueStatut> findByTypeDocumentAndDocumentIdOrderByDateChangementAsc(String typeDocument, Long documentId);
}

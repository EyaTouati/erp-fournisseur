package tn.erp.fournisseur.b.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.b.entity.DemandeAchat;
import tn.erp.fournisseur.b.entity.StatutDemandeAchat;

public interface DemandeAchatRepository extends JpaRepository<DemandeAchat, Long> {
    List<DemandeAchat> findByStatut(StatutDemandeAchat statut);
}
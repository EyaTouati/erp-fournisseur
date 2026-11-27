package tn.erp.fournisseur.c.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.erp.fournisseur.c.entity.Commande;
import tn.erp.fournisseur.c.entity.StatutCommande;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    boolean existsByOffreId(Long offreId);

    List<Commande> findByStatutOrderByIdDesc(StatutCommande statut);

    List<Commande> findAllByOrderByIdDesc();
}
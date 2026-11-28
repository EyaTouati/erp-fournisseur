package tn.erp.fournisseur.a.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.erp.fournisseur.a.entity.Fournisseur;
import tn.erp.fournisseur.a.entity.StatutFournisseur;

import java.util.List;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    boolean existsByIdentifiantFiscal(String identifiantFiscal);

    boolean existsByIdentifiantFiscalAndIdNot(String identifiantFiscal, Long id);

    List<Fournisseur> findByStatut(StatutFournisseur statut);

    /** Fournisseurs qui ont ce produit dans leur catalogue (le filtre ACTIF est fait dans le service). */
    @Query("select c.fournisseur from Catalogue c where c.produitId = :produitId")
    List<Fournisseur> findFournisseursByProduitId(@Param("produitId") Long produitId);

    /** Table commande créée par la V4 (sous-processus C) : fonctionne dès que la V4 est fusionnée. */
    @Query(value = "SELECT count(*) FROM commande WHERE fournisseur_id = :id "
            + "AND statut NOT IN ('CLOTUREE','ANNULEE')", nativeQuery = true)
    long compterCommandesNonCloturees(@Param("id") Long id);
}

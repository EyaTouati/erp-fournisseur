
package tn.erp.fournisseur.a.fournisseur;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    boolean existsByIdentifiantFiscal(String identifiantFiscal);

    boolean existsByNomIgnoreCase(String nom);
}

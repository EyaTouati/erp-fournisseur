package tn.erp.fournisseur.commun.integration;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.erp.fournisseur.commun.erreurs.RegleGestionException;

import java.math.BigDecimal;

/**
 * Simulation du module Comptabilité (dossier de référence, section 11).
 * Appelée par D : type FACTURE_VALIDEE (référence = facture) ou PAIEMENT (référence = paiement).
 */
@Service
public class ComptaService {

    private final EcritureComptableRepository ecritures;

    public ComptaService(EcritureComptableRepository ecritures) {
        this.ecritures = ecritures;
    }

    @Transactional
    public EcritureComptable envoyer(String type, Long referenceId, BigDecimal montant) {
        if (montant == null || montant.signum() <= 0) {
            throw new RegleGestionException("COMPTA", "Le montant de l'écriture doit être positif");
        }
        return ecritures.save(new EcritureComptable(type, referenceId, montant));
    }
}
package tn.erp.fournisseur.commun.erreurs;

/** Ressource demandée inexistante (commande, facture, paramètre...). Renvoyée en HTTP 404. */
public class IntrouvableException extends RuntimeException {

    public IntrouvableException(String message) {
        super(message);
    }
}
